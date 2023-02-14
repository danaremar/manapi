package com.manapi.manapigateway.jwt;

import java.util.Collection;
import java.util.List;

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

    private List<String> features;

    private Collection<? extends GrantedAuthority> authorities;

}
