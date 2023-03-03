package com.manapi.manapiproject.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin
public class TestController {

    @PostMapping(value = "/project/{projectId}/projects/test")
    public ResponseEntity<Object> getSprintExample(@PathVariable String projectId, @RequestHeader Map<String, String> headers) {
        return new ResponseEntity<>("Todo ok", HttpStatus.OK);
    }
    
}
