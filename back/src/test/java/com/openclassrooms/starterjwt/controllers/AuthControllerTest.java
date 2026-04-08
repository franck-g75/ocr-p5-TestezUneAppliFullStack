package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @Autowired
    private PasswordEncoder passwordEncoder;

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





    @Test
    void shouldLoginSuccessfully_IT() throws Exception {
        
        //GIVEN

        userId= 1L;
        userMock = new User();
        userMock.setId(userId);
        userMock.setEmail("yoga@studio.com");
        userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword(passwordEncoder.encode("test!1234"));
        userMock.setAdmin(true);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");


        Authentication authentication = mock(Authentication.class);

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, "yoga@studio.com", "Alice", "tortellini",true, "test!1234");

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);    

        Mockito.when(authentication.getPrincipal()).thenReturn((userDetails));

        Mockito.when(jwtUtilsMocked.generateJwtToken(authentication)).thenReturn("theJWT");

        Mockito.when(userService.findById(userId)).thenReturn(userMock);
        
        Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));
    
        //WHEN
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        //THEN
        assertEquals(200, response.getStatusCodeValue());

        JwtResponse jwtResponse = (JwtResponse)response.getBody();

        assertEquals("theJWT", jwtResponse.getToken());
        assertEquals("yoga@studio.com", jwtResponse.getUsername());
        assertEquals("Alice", jwtResponse.getFirstName());
        assertEquals("tortellini", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());

        verify(authenticationManager).authenticate(any());
        verify(jwtUtilsMocked).generateJwtToken(authentication);
        verify(userRepository).findByEmail("yoga@studio.com");


    }

    @Test
    void shouldLoginSuccessfully_UserNotFoundInDB_IT() throws Exception {
        
        //GIVEN

        userId= 1L;
        userMock = new User();
        userMock.setId(userId);
        userMock.setEmail("yoga@studio.com");
        userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword(passwordEncoder.encode("test!1234"));
        userMock.setAdmin(true);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!12345");


        Authentication authentication = mock(Authentication.class);

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, "yoga@studio.com", "Alice", "tortellini",true, "test!1234");

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);    

        Mockito.when(authentication.getPrincipal()).thenReturn((userDetails));

        Mockito.when(jwtUtilsMocked.generateJwtToken(authentication)).thenReturn("theJWT");

        Mockito.when(userService.findById(userId)).thenReturn(userMock);
        
        Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.empty());  //not found
    
        //WHEN
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        //THEN
        assertEquals(200, response.getStatusCodeValue());

        JwtResponse jwtResponse = (JwtResponse)response.getBody();

        assertEquals("theJWT", jwtResponse.getToken());
        assertEquals("yoga@studio.com", jwtResponse.getUsername());
        assertEquals("Alice", jwtResponse.getFirstName());
        assertEquals("tortellini", jwtResponse.getLastName());
        assertFalse(jwtResponse.getAdmin());                                //not admin

        verify(authenticationManager).authenticate(any());
        verify(jwtUtilsMocked).generateJwtToken(authentication);
        verify(userRepository).findByEmail("yoga@studio.com");

    }

    @Test
    void shouldRaiseBadCredentialException() throws Exception{

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!12345");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad pwd"));

        assertThrows(BadCredentialsException.class, ()-> authController.authenticateUser(loginRequest));

        verify(authenticationManager).authenticate(any());

    }


}
