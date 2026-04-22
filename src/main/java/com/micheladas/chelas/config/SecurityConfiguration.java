package com.micheladas.chelas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.micheladas.chelas.service.UserService;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Main security configuration class that defines the authentication provider,
 * authorization rules, session management policies, and security headers.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

	private final UserService usuarioServicio;
	private final BCryptPasswordEncoder passwordEncoder;

	public SecurityConfiguration(UserService usuarioServicio,
                                 BCryptPasswordEncoder passwordEncoder) {
		this.usuarioServicio = usuarioServicio;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(usuarioServicio);
		auth.setPasswordEncoder(passwordEncoder);
		return auth;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(authorize -> authorize

						.requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
						.requestMatchers("/login", "/registro/**", "/auth/**", "/error/403").permitAll()
						.requestMatchers("/loadForgotPassword", "/forgotPassword").permitAll()
						.requestMatchers("/loadResetPassword/**", "/changePassword/**").permitAll()
						.requestMatchers("/usuarios/bloquear/**").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("email")
						.passwordParameter("password")
						.defaultSuccessUrl("/index", true)
						.permitAll()
				)

				.sessionManagement(session -> session
						.sessionFixation().migrateSession()
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.invalidSessionUrl("/login")
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

		http.headers(headers -> headers
				// Anti-Clickjacking
				.frameOptions(frame -> frame.sameOrigin())

				// PROTECTION XSS
				.xssProtection(xss -> xss.headerValue(org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))

				// Content Security Policy
				/* UNBLOCK IN PRODUCTION
				.contentSecurityPolicy(csp -> csp
						.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; upgrade-insecure-requests;")
				)*/
		);

		return http.build();
	}

}