package com.manapi.manapigateway.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

import com.manapi.manapigateway.jwt.JwtAuthenticationManager;
import com.manapi.manapigateway.jwt.JwtAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@ConfigurationProperties("manapi")
public class WebSecurityConfig {

	@Value("${manapi.security.protection.xss}")
	public Boolean enableXssProtection = Boolean.TRUE;

	@Value("${manapi.security.protection.csrf}")
	public Boolean enableCsrfProtection = Boolean.TRUE;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthenticationManager jwtAuthenticationManager, JwtAuthenticationConverter jwtAuthenticationConverter, RateFilter rateFilter) {

		AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);

		return http
				.csrf().disable()
				.authorizeExchange()
					.pathMatchers("/login", "/register", "/v3/*", "/configuration/ui",
							"/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/swagger-ui/*",
							"/webjars/**", "/resources/**")
						.permitAll()
					.anyExchange()
						// .permitAll()
						.authenticated()
				.and()
					.exceptionHandling()
						.authenticationEntryPoint((exchange, e) -> 
							Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
						)
						.accessDeniedHandler((exchange, e) -> 
							Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
					)

				// FILTERS
				.and()

					// AUTHENTICATION
					.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
					
					// RATE LIMIT
					// .addFilterAfter(rateFilter, SecurityWebFiltersOrder.AUTHENTICATION)
					
				.build();

	}

}
