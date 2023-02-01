package com.manapi.manapigateway.rest;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manapi.manapigateway.exceptions.users.DuplicatedEmail;
import com.manapi.manapigateway.exceptions.users.DuplicatedUsername;
import com.manapi.manapigateway.jwt.JwtDto;
import com.manapi.manapigateway.jwt.JwtService;
import com.manapi.manapigateway.model.users.UserCreateDto;
import com.manapi.manapigateway.model.users.UserLoginDto;
import com.manapi.manapigateway.model.util.Message;
import com.manapi.manapigateway.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication")
@RequestMapping
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

    @PostMapping(value = "/login")
	public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDto userDto) {

		Authentication authentication;

		try {
			authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					userDto.getUsername(), userDto.getPassword(), Collections.emptyList()));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message("User or password is invalid"));
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwt = this.jwtService.generateToken(userDetails);

		JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());

		return new ResponseEntity<>(jwtDto, HttpStatus.ACCEPTED);
	}

	@PostMapping(value = "/register")
	public ResponseEntity<Object> register(@RequestBody @Valid UserCreateDto userDto) {
		try {
			userService.addUser(userDto);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (DuplicatedUsername e) {
			String message = "Username is duplicated";
			return new ResponseEntity<>(new Message(message), HttpStatus.CONFLICT);
		} catch (DuplicatedEmail e) {
			String message = "Email is duplicated";
			return new ResponseEntity<>(new Message(message), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    
}
