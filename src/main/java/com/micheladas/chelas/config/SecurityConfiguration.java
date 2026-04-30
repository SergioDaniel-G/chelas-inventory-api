package com.micheladas.chelas.config;

import com.micheladas.chelas.authservice.IpService;
import com.micheladas.chelas.authservice.MfaEmailService;
import com.micheladas.chelas.entity.UserAccount;
import com.micheladas.chelas.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.micheladas.chelas.service.UserService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Main security configuration class that defines the authentication provider,
 * authorization rules, session management policies, and security headers.
 */

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
		/*
		 * [PROD-ACTION]: SET TO TRUE IN PRODUCTION.
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
						.requestMatchers("/login", "/registro/**", "/auth/**", "/error/403").permitAll()
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
						.defaultSuccessUrl("/index")
						.successHandler((request, response, authentication) -> {
							String email = authentication.getName();
							if (ipService.isKnownIp(email, request.getRemoteAddr())) {

								// LOAD ACTUAL ROLES HERE AS WELL TO AVOID DEGRADING THE ADMIN
								UserAccount user = userRepository.findByEmail(email);
								var authorities = user.getRoles().stream()
										.map(r -> new SimpleGrantedAuthority(r.getName()))
										.collect(Collectors.toList());

								Authentication fullAuth = new UsernamePasswordAuthenticationToken(
										authentication.getPrincipal(), null, authorities);

								var context = SecurityContextHolder.getContext();
								context.setAuthentication(fullAuth);
								new HttpSessionSecurityContextRepository().saveContext(context, request, response);

								response.sendRedirect("/index");
							} else {
								mfaEmailService.sendOtpEmail(email);
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