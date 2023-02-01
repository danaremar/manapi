package com.manapi.manapigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.manapi.manapigateway.jwt.JwtAuthenticationFilter;
import com.manapi.manapigateway.jwt.JwtEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@ConfigurationProperties("manapi")
public class SecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtEntryPoint jwtEntryPoint;

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("${manapi.security.protection.xss}")
	public Boolean enableXssProtection = Boolean.TRUE;

	@Value("${manapi.security.protection.csrf}")
	public Boolean enableCsrfProtection = Boolean.TRUE;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(6);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// XSS protection
		if (Boolean.TRUE.equals(enableXssProtection)) {
			http.headers().xssProtection().and().contentSecurityPolicy("script-src 'self'");
		}

		// CSRF protection
		if (Boolean.FALSE.equals(enableCsrfProtection)) {
			// deepcode ignore DisablesCSRFProtection: Is normally enabled CSRF
			http.csrf().disable();
		}

		http.authorizeRequests()
				.antMatchers("/login", "/register", "/v3/api-docs", "/v3/api-docs/*", "/configuration/ui",
						"/swagger-resources/**", "/configuration/security", "/swagger-ui/*", "/webjars/**", "/resources/**")
					.permitAll()
				.anyRequest()
					.authenticated()
				.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.exceptionHandling()
					.authenticationEntryPoint(jwtEntryPoint)
				.and()
					.authenticationProvider(authenticationProvider())
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
					.userDetailsService(userDetailsService);

		return http.build();
	}

}
