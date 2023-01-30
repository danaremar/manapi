package com.manapi.manapigateway.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.manapi.manapigateway.model.users.UserCreateDto;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
	private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        // user
        UserCreateDto user1 = new UserCreateDto();
        user1.setUsername("user1");
        user1.setPassword("asdkn1a4f?aADH");
        user1.setEmail("user1@test.com");
        user1.setName("Juanito");
        user1.setLastName("Nogtir");
        user1.setSector("Software");
        user1.setCountry("ES");

    }

    @Test
    void testCreateUser() throws Exception {

        JSONObject json = new JSONObject();
        json.put("username", "test");
        json.put("password", "test_1234TEST");
        json.put("email", "test@test.com");
        json.put("name", "Test");
        json.put("lastName", "Testito");
        json.put("Sector", "Software");
        json.put("Contry", "test");

        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json.toString()))

				// Validate the response code and content type
				.andExpect(status().isCreated());

    }

    @Test
    void testLoginUser() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "test");
        json.put("password", "test_1234TEST");

        this.mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json.toString()))

				// Validate the response code and content type
				.andExpect(status().is2xxSuccessful());

    }
    
}
