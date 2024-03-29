package com.manapi.manapigateway.jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import com.manapi.manapigateway.model.user.User;
import com.manapi.manapigateway.configuration.ManapiMessages;

@Component
public class JwtService {

	private final Logger log = LoggerFactory.getLogger(JwtService.class);

	@Value("${manapi.security.jwt.secret}")
	private String secret;

	@Value("${manapi.security.jwt.expiration}")
	private Long expiration;

	// @Autowired
	// private ProjectRoleService projectRoleService;

	/**
	 * Get token from request
	 * @param req
	 * @return
	 */
	public String getTokenFromRequest(ServerHttpRequest req) {
		List<String> ls = req.getHeaders().get("Authorization");
		if(ls==null) {
			return null;
		} else {
			return ls.get(0).replace("Bearer ", "");
		}
	}

	/**
	 * Get all claims from token (claims is all properties set up bearer token)
	 * 
	 * @param token
	 * @return
	 */
	public Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Get user id from bearer token
	 * 
	 * @param token
	 * @return
	 */
	public String getUserIdFromToken(String token) {
		return getClaimsFromToken(token).getSubject();
	}

	// TODO: test it -> format []
	/**
	 * Get feature groups by given token
	 * @param token
	 * @return
	 */
	public List<String> getFeatureGroupsFromToken(String token) {
		return (List<String>) getClaimsFromToken(token).get("featureGroups");
	}

	/**
	 * Generate JWT bearer token by user with default claims
	 * 
	 * @param user
	 * @return
	 */
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();

		// PLAN
		claims.put("plan", user.getActualPlan().getType());

		// FEATURE GROUP
		claims.put("featureGroups", user.getActiveFeatureGroups());

		// FEATURES
		claims.put("features", user.getActiveFeatures());

		return generateToken(claims, user);
	}

	/**
	 * Generate JWT bearer token by adding new claims
	 * 
	 * @param extraClaims
	 * @param user
	 * @return
	 */
	public String generateToken(Map<String, Object> extraClaims, User user) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(user.getId())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 1000 * expiration))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	/**
	 * Validate if token is correct
	 * 
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token) {
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
