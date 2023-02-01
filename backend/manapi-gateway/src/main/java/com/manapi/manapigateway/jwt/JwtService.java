package com.manapi.manapigateway.jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.manapi.manapigateway.config.ManapiMessages;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtService {

	private final Logger log = LoggerFactory.getLogger(JwtService.class);

	@Value("${manapi.security.jwt.secret}")
	private String secret;

	@Value("${manapi.security.jwt.expiration}")
	private Long expiration;

	public String getUsernameFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * expiration))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public boolean valitateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.debug("validateToken() - Expired jwt bearer token");
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ManapiMessages.TOKEN_EXPIRED);
		} catch (Exception e) {
			log.debug("validateToken() - An exception happened: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ManapiMessages.TOKEN_BAD_FORMAT);
		}
	}

}
