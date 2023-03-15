package com.manapi.manapigateway.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.manapi.manapigateway.config.ManapiMessages;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtService jwtService;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private RateLimiterService rateLimiterService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = getToken(request);
		if (token != null && jwtService.valitateToken(token)) {

			// AUTHENTICATION & AUTHORIZATION
			String username = jwtService.getUsernameFromToken(token);
			PrincipalUser principalUser = userDetailsService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principalUser, null,
			principalUser.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);

			// RATE LIMITER
			// Returns remaining requests & throw error if cuota is exceeded
			Bucket bucket = rateLimiterService.resolveBucket(username);
			ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
			if (probe.isConsumed()) {
				response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
			} else {
				long secToRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
				response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(secToRefill));
				response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
				throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, ManapiMessages.RATE_EXCEEDED);
			}

		}

		filterChain.doFilter(request, response);

	}

	private String getToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer")) {
			return header.replace("Bearer ", "");
		} else {
			return null;
		}
	}

}
