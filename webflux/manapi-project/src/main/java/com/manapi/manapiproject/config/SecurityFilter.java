package com.manapi.manapiproject.config;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// public class SecurityFilter {}

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-user-id");
        String projectId = request.getHeader("X-project-id");
        String role = request.getHeader("X-user-role");
        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority(role));

        // WITH PRINCIPAL USER
        // UserDetails userDetails = new User(userId, "nopass", roles);

        // WITH HEADER CLASS
        HeaderInfo headerInfo = new HeaderInfo(userId, projectId, role);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(headerInfo, null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class HeaderInfo {

        private String userId;

        private String projectId;

        private String role;

    }
    
}
