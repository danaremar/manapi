package com.manapi.manapigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manapi.manapigateway.dto.user.UserCreateDto;
import com.manapi.manapigateway.dto.user.UserLoginDto;
import com.manapi.manapigateway.jwt.JwtDto;
import com.manapi.manapigateway.service.UserService;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication")
@RequestMapping
@CrossOrigin
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginDto userDto) {
        JwtDto jwtDto = userService.getJwtFromUser(userDto);
        return new ResponseEntity<>(jwtDto, HttpStatus.CREATED);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.addUser(userCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
