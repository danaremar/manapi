package com.manapi.manapigateway.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpHeaders;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        return Mono.justOrEmpty(exchange)
                .flatMap(serverWebExchange -> Mono.justOrEmpty(
                        serverWebExchange
                                .getRequest()
                                .getHeaders()
                                .getFirst(HttpHeaders.AUTHORIZATION)))
                .filter(header -> !header.trim().isEmpty() && header.trim().startsWith(AUTH_HEADER_VALUE_PREFIX))
                .map(header -> header.substring(AUTH_HEADER_VALUE_PREFIX.length()))
                .map(token -> new UsernamePasswordAuthenticationToken(token, token));
                
    }

}
