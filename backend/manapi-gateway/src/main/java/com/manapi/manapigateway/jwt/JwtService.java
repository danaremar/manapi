package com.manapi.manapigateway.jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.manapi.manapicommon.model.users.FeatureType;
import com.manapi.manapigateway.config.ManapiMessages;
import com.manapi.manapigateway.model.users.User;
import com.manapi.manapigateway.service.ProjectService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.http.HttpStatus;

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

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();

		// PLAN
		claims.put("plan", user.getPlan().name());

		// FEATURES
		if (user.getSubscriptions()!=null) {
			List<String> features = user.getSubscriptions().stream()
					.map(x -> x.getFeatureType().name())
					.toList();
			claims.put("features", features);
		} else {
			claims.put("features", List.of(FeatureType.PROJECT.name()));
		}

		// PROJECTS
		if (user.getProjectRoles()!=null) {
			Map<String, String> projectsRoles = new HashMap<>();
			user.getProjectRoles().stream()
					.forEach(x -> projectsRoles.put(x.getProject().getId().toString(), x.getRole().toString()));
			claims.put("projects", projectsRoles);
		}

		return generateToken(claims, user);
	}

	public String generateToken(Map<String, Object> extraClaims, User user) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(user.getUsername())
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
