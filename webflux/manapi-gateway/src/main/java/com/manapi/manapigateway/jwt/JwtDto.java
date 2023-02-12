package com.manapi.manapigateway.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// @AllArgsConstructor
@NoArgsConstructor
public class JwtDto {

    private String bearer = "Bearer";

    private String token;

    private String username;

    private Collection<? extends GrantedAuthority> authorities;

}
