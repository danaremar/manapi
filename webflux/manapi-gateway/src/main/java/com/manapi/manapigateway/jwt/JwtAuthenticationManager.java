package com.manapi.manapigateway.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import reactor.core.publisher.Mono;

import com.manapi.manapigateway.model.user.User;
import com.manapi.manapigateway.service.UserService;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        return Mono.just(jwtService.validateToken(token))
                .filter(x -> x)
                .switchIfEmpty(Mono.empty())
                .map(x -> {

                    String username = jwtService.getUsernameFromToken(token);
                    User user = userService.findUserByUsername(username);
                    PrincipalUser principalUser = PrincipalUser.build(user);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principalUser,
                            null,
                            principalUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    return auth;
                });

    }

}
