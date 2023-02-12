package com.manapi.manapigateway.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
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
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
// @EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@ConfigurationProperties("manapi")
public class WebSecurityConfig {

    // @Autowired
	// UserDetailsService userDetailsService;

	// @Autowired
	// JwtEntryPoint jwtEntryPoint;

	// @Autowired
	// JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("${manapi.security.protection.xss}")
	public Boolean enableXssProtection = Boolean.TRUE;

	@Value("${manapi.security.protection.csrf}")
	public Boolean enableCsrfProtection = Boolean.TRUE;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(6);
	}

	@Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
			.csrf().disable()
			.authorizeExchange()
				.pathMatchers("/login", "/register", "/v3/*", "/configuration/ui",
				"/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/swagger-ui/*", "/webjars/**", "/resources/**")
				.permitAll()
			.anyExchange()
				.permitAll()
			.and()
			.build();
        
    }

}
