package com.manapi.manapigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manapi.manapigateway.dto.user.UserShowDto;
import com.manapi.manapigateway.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@Tag(name = "Users")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/my-profile")
	public Mono<ResponseEntity<Object>> getMyProfile() {
		Mono<UserShowDto> user = userService.showMyProfile();
        return user.map(x -> new ResponseEntity<>(user, HttpStatus.OK));
	}

    
    
}
