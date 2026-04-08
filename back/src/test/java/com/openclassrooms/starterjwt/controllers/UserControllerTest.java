package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
       
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository; //bean mocké

    @MockBean
    private UserService userService; //bean mocké

    Long userId;
    Long userIdDeleted;
    User userMock;
    User userMockDeleted;
    Authentication auth;
    Authentication authenticated ;
    String jwt;


    @BeforeEach
    void beforeEach(){
      LocalDateTime localDateTimeNow = LocalDateTime.now();
      userId= 1L;
      userMock = new User("yoga@studio.com","tortellini","Alice",passwordEncoder.encode("test!1234"),false);
      userMock.setId(userId);
      //userMock.setEmail("yoga@studio.com");
      //userMock.setFirstName("Alice");
      //userMock.setLastName("tortellini");
      //userMock.setPassword(passwordEncoder.encode("test!1234"));
      //userMock.setAdmin(false);
      userMock.setCreatedAt(localDateTimeNow);
      userMock.setUpdatedAt(localDateTimeNow);



      Mockito.when(userService.findById(userId)).thenReturn(userMock);
      Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));
    
      

      auth = new UsernamePasswordAuthenticationToken("yoga@studio.com","test!1234");
      authenticated = authenticationManager.authenticate(auth);

      jwt = jwtUtils.generateJwtToken(authenticated);

    }



    @Test
    void shouldFindAndReturnUserNumber1_IT() throws Exception {
      
      mockMvc.perform(
        get("http://localhost:8080/api/user/{id}",userId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.email").value("yoga@studio.com"))
        .andExpect(jsonPath("$.firstName").value("Alice"))
        .andExpect(jsonPath("$.lastName").value("tortellini"));
      
    }


    @Test
    void shouldNotFindAndReturn4xx_badId_IT() throws Exception {
      
      mockMvc.perform(
        get("http://localhost:8080/api/user/{id}",userId+1) //<-- +1 is important !!!
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$").doesNotExist());
      
    }


    @Test
    void shouldNotFindUserAndReturn4xx_badNumberFormat_IT() throws Exception {
            
      mockMvc.perform(
          get("http://localhost:8080/api/user/a") //<-- a is important !!!
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$").doesNotExist());

    }


    @Test
    void shouldFindAndDeleteUserNumber1_IT() throws Exception {
      
      mockMvc.perform(
        delete("http://localhost:8080/api/user/{id}",userId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
      
    }


    @Test
    void shouldNotFindAndNotDeleteUserAndReturn4xx_badId_IT() throws Exception {

      mockMvc.perform(
        delete("http://localhost:8080/api/user/{id}",userId+1)  //<-- +1 is important !!!
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$").doesNotExist());
      
    }

    @Test
    void shouldNotFindAndNotDeleteUserAndReturn4xx_badNumberFormat_IT() throws Exception {
      
      
      mockMvc.perform(
          delete("http://localhost:8080/api/user/a") //<-- a is important !!!
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$").doesNotExist());

    }


    @Test
    void shouldNotDeleteUserAndReturn4xx_UserLoggedIsNotUserDeleted_IT() throws Exception {
      
      Long userBisId= 2L;
      LocalDateTime localDateTimeNow = LocalDateTime.now();
      User userBisMock = new User(
        userBisId,
        "alice@totellini.com",
        "tortellini",
        "Alice",
        passwordEncoder.encode("test!1234"),
        false,
        localDateTimeNow,
        localDateTimeNow
      );
      //userBisMock.setId(userBisId);
      //userBisMock.setEmail("alice@totellini.com");
      //userBisMock.setFirstName("Alice");
      //userBisMock.setLastName("tortellini");
      //userBisMock.setPassword(passwordEncoder.encode("test!1234"));
      //userBisMock.setAdmin(false);
      
      Mockito.when(userService.findById(userBisId)).thenReturn(userBisMock);
      Mockito.when(userRepository.findByEmail(userBisMock.getEmail())).thenReturn(Optional.of(userBisMock));

        mockMvc.perform(
          delete("http://localhost:8080/api/user/2") //<-- 2 is important !!!
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$").doesNotExist());

    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
  }

