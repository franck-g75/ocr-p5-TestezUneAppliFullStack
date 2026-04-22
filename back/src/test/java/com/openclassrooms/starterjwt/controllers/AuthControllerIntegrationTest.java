package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtilsMocked;

    @MockBean
    private UserRepository userRepository; //bean mocké

    @MockBean
    private UserService userService; //bean mocké

    Long userId;
    User userMock;
    String jwt;

    @Test
    void shouldRegister_EmailAlreadyTaken_IT() throws Exception {
       
        Mockito.when(userRepository.existsByEmail("yoga@studio.com")).thenReturn(true);

        String json = "{\"email\":\"yoga@studio.com\",\"password\":\"test!1234\",\"firstName\":\"premon\",\"lastName\":\"NOM\"}";//login password firstName lastName
        
        MvcResult result = mockMvc.perform(
            post("http://localhost:8080/api/auth/register") //<-- no jwt header needed
            .contentType("application/json")
            .content(json))
            .andExpect(status().isBadRequest())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\"Error: Email is already taken!\"}", content);

    }







    @Test
    void shouldRegisterWithoutError_IT() throws Exception {
       
        Mockito.when(userRepository.existsByEmail("yoga@studio.com")).thenReturn(false);

        String json = "{\"email\":\"user@studio.com\",\"password\":\"test!1234\",\"firstName\":\"premon\",\"lastName\":\"NOM\"}";//login password firstName lastName
        
        MvcResult result = mockMvc.perform(
            post("http://localhost:8080/api/auth/register") //<-- no jwt header needed
            .contentType("application/json")
            .content(json))
            .andExpect(status().isOk())
            .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals("{\"message\":\"User registered successfully!\"}", content);

    }






}
