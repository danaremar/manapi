package com.manapi.manapigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manapi.manapigateway.dto.UserCreateDto;
import com.manapi.manapigateway.dto.UserLoginDto;
import com.manapi.manapigateway.jwt.JwtDto;
import com.manapi.manapigateway.service.UserService;
import javax.validation.Valid;

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
        try {
            JwtDto jwtDto = userService.getJwtFromUser(userDto);
            return new ResponseEntity<>(jwtDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserCreateDto userCreateDto) {
        try {
            userService.addUser(userCreateDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}