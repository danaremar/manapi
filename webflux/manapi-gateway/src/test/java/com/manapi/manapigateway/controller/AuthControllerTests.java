package com.manapi.manapigateway.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.manapi.manapigateway.dto.user.UserCreateDto;
import com.manapi.manapigateway.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {

        // user1
        UserCreateDto user1 = new UserCreateDto();
        user1.setUsername("user1");
        user1.setPassword("asdkn1a4f?aADH");
        user1.setEmail("user1@test.com");
        user1.setFirstName("Juanito");
        user1.setLastName("Nogtir");
        user1.setCountry("ES");
        userService.addUser(user1);

    }

    @Test
    void testCreateUser() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "testXD");
        json.put("password", "test_1234TEST");
        json.put("email", "test_xd@test.com");
        json.put("name", "Test");
        json.put("lastName", "Testito");
        json.put("sector", "Software");
        json.put("country", "ES");
        this.mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andExpect(status().isCreated());

    }

    @Test
    void testLoginUserFailed() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "test");
        json.put("password", "test_1234TEST");
        this.mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(json.toString()))
				.andExpect(status().is4xxClientError());
    }
    
}
