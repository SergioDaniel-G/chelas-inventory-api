package com.micheladas.chelas.config;

import com.micheladas.chelas.authservice.IpService;
import com.micheladas.chelas.authservice.MfaEmailService;
import com.micheladas.chelas.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.micheladas.chelas.service.UserService;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import java.util.Arrays;


@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;
	private final Environment env;
	private final MfaEmailService mfaEmailService;
	private final IpService ipService;
	private final UserRepository userRepository;

	public SecurityConfiguration(UserService userService,
								 BCryptPasswordEncoder passwordEncoder,
								 Environment env,
								 MfaEmailService mfaEmailService,
								 IpService ipService,
								 UserRepository userRepository) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.env = env;
		this.mfaEmailService = mfaEmailService;
		this.ipService = ipService;
		this.userRepository = userRepository;
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder);

		/* [PROD-ACTION]: SET TO TRUE IN PRODUCTION.
		 * THIS PREVENT ENUMERATION ATTACKS. PREVENTING AN ATTACKING FROM KNOWING WHICH EMAILS EXISTS IN YOUR DB
		 */

		auth.setHideUserNotFoundExceptions(false);
		return auth;
	}

	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(authenticationProvider())
				.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// CKECH IF THE LOCAL PROFILE IS ACTIVE
		boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("local");

		http
				.authorizeHttpRequests(authorize -> authorize

						.requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
						.requestMatchers("/login", "/registro/**", "/error/403").permitAll()
						.requestMatchers("/loadForgotPassword", "/forgotPassword").permitAll()
						.requestMatchers("/loadResetPassword/**", "/changePassword/**").permitAll()
						.requestMatchers("/usuarios/bloquear/**").hasRole("ADMIN")
						.requestMatchers("/verificar-codigo", "/auth/validar-otp").hasRole("PRE_VERIFIED")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("email")
						.passwordParameter("password")
						//.defaultSuccessUrl("/index")
						.successHandler((request, response, authentication) -> {
							// CACHE CLEAN
							new org.springframework.security.web.savedrequest.HttpSessionRequestCache()
									.removeRequest(request, response);

							String email = authentication.getName();
							String ip = request.getRemoteAddr(); // OBTAIN THE IP

							// ¿THE IP IS KNOW?
							if (ipService.isIpKnown(email, ip)) {

								// CASE: IP IS KNOW (DIRECT ACCESS)
								System.out.println("DEBUG: IP conocida para " + email + ". Saltando MFA.");

								// SEARCH REAL USERS TO ASSIGN ACTUAL ROLES
								var user = userRepository.findByEmail(email);
								var realAuthorities = user.getRoles().stream()
										.map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
										.collect(java.util.stream.Collectors.toList());

								var fullAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
										authentication.getPrincipal(), null, realAuthorities);

								org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(fullAuth);

								// SAVE AND TO THE INDEX
								new org.springframework.security.web.context.HttpSessionSecurityContextRepository()
										.saveContext(org.springframework.security.core.context.SecurityContextHolder.getContext(), request, response);

								response.sendRedirect("/index");

							} else {

								// CASE: NEW IP (REQUESTING VERIFICATION CODE)
								System.out.println("DEBUG: IP DESCONOCIDA (" + ip + "). Enviando MFA.");

								mfaEmailService.sendOtpEmail(email);

								ipService.registerAccessAttempt(email, "LOGIN_INICIAL", "Nueva IP detectada", request);

								var partialAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
										authentication.getPrincipal(), null,
										java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_PRE_VERIFIED"))
								);

								org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(partialAuth);

								new org.springframework.security.web.context.HttpSessionSecurityContextRepository()
										.saveContext(org.springframework.security.core.context.SecurityContextHolder.getContext(), request, response);

								response.sendRedirect("/verificar-codigo");
							}
						})
						.permitAll()
				)

				.sessionManagement(session -> session
						.sessionFixation().migrateSession()
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.invalidSessionUrl("/login?timeout") // REDIRECT WHEN SESSION EXPIRES
						/*
						 * [PROD-ACTION]: EVALUATE IF 'maximumSessions(1)' IS VERY STRICT FOR USERS.
						 */
						.maximumSessions(1)
						.expiredUrl("/login?expired")
				)

				.exceptionHandling(exception -> exception
						.accessDeniedPage("/error/403")
				)

				.logout(logout -> logout
						.logoutUrl("/logout")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				);
		/*
		 * [PROD-ACTION]: ENSURE THE SERVER HAS SSL HTTPS CONFIGURED.
		 * IF idDEV IS FALSE AND YOU DON´T HAVE HTTPS THE APP WILL ENTER AN INFINITE REDIRECT LOOP.
		 */
		if (!isDev) {
			http.requiresChannel(channel -> channel
					.anyRequest().requiresSecure()
			);
		}

		http.headers(headers -> {

			// Anti-Clickjacking
			headers.frameOptions(frame -> frame.sameOrigin());

			// PROTECTION XSS
			headers.xssProtection(xss -> xss.headerValue(org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));

			/*
			 * [PROD-ACTION]: UNBLOCKED.
			 * THE CONTENT SECURITY POLICY IS VITAL. ENSURE THAT ALL YOURS SOURCES FONTS,
			 * EXTERNAL SCRIPTS (reCAPTCHA) AND STYLES ARE LISTED HERE.
			 */
             /*
				.contentSecurityPolicy(csp -> csp
						.policyDirectives("default-src 'self'; " +
              // Allows local scripts and those required by Google reCAPTCHA.
              "script-src 'self' 'unsafe-inline' https://www.google.com/recaptcha/ https://www.gstatic.com/recaptcha/; " +
              // Allows reCAPTCHA to load its own frames.
              "frame-src 'self' https://www.google.com/recaptcha/ https://recaptcha.google.com/; " +
              // Allows local and external styles and fonts.
              "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com/; " +
              "font-src 'self' https://fonts.gstatic.com/; " +
              // API and data connection permissions.
              "connect-src 'self'; " +
              "img-src 'self' data:; " +
              // Enforces secure HTTPS connections only.
              "upgrade-insecure-requests;")
				)*/
			// 3. HSTS CONDITIONAL
			headers.httpStrictTransportSecurity(hsts -> {
				if (!isDev) {
					hsts.includeSubDomains(true)
							.maxAgeInSeconds(31536000) // 1 YEAR
							.preload(true);
				} else {
					hsts.disable();
				}
			});
		});

		return http.build();
	}

}