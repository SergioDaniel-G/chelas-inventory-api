package com.micheladas.chelas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.micheladas.chelas.servicio.UsuarioServicio;

@Configuration
public class SecurityConfiguration {

	private final UsuarioServicio usuarioServicio;
	private final BCryptPasswordEncoder passwordEncoder;

	public SecurityConfiguration(UsuarioServicio usuarioServicio,
								 BCryptPasswordEncoder passwordEncoder) {
		this.usuarioServicio = usuarioServicio;
		this.passwordEncoder = passwordEncoder;
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
						// 1. Recursos estáticos (CSS, JS, Imágenes)
						.requestMatchers("/css/**", "/js/**", "/img/**").permitAll()

						// Agrupamos las URLs que definimos en el controlador
						.requestMatchers("/login", "/registro/**", "/auth/**").permitAll()
						.requestMatchers("/loadForgotPassword", "/forgotPassword").permitAll()
						.requestMatchers("/loadResetPassword/**", "/changePassword/**").permitAll()

						// 3. Seguridad por ROLES (ADMIN)
						.requestMatchers("/caguamas/editar/**", "/caguamas/eliminar/**").hasRole("ADMIN")

						// 4. El resto del ERP requiere estar logueado
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login") // La URL de tu @GetMapping
						.usernameParameter("email")
						.passwordParameter("password")
						.successHandler(authenticationSuccessHandler())
						.permitAll()
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				);

		return http.build();
	}
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler("/index");
	}
}