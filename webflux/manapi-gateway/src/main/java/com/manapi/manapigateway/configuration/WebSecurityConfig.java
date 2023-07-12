package com.manapi.manapigateway.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

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
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
			JwtAuthenticationManager jwtAuthenticationManager, JwtAuthenticationConverter jwtAuthenticationConverter,
			RateFilter rateFilter) {

		AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(jwtAuthenticationManager);
		authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);

		return http
                .csrf(x -> x.disable())
				.cors(x -> x.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/login", "/register", "/v3/**", "/configuration/ui",
                                "/swagger-resources/**", "/configuration/security", "/swagger-ui.html", "/swagger-ui/*",
                                "/webjars/**", "/resources/**")
                        .permitAll()
                        .pathMatchers("/project/*/projects/**")
                        .authenticated()
                        .anyExchange()
                        // .permitAll()
                        .authenticated())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((exchange, e) -> Mono
                                .fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((exchange, e) -> Mono
                                .fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))))

                // AUTHENTICATION
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                // RATE LIMIT
                // .addFilterAfter(rateFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .build();

	}

	// @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:8080",
				"https://danaremar.github.io", "https://web-danaremar.cloud.okteto.net"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
