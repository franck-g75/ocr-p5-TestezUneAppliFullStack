package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
//@SpringBootTest         //doesn't work when database is down
//@AutoConfigureMockMvc
public class TeacherControllerIntegration {
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

    @MockBean
    private TeacherRepository teacherRepository; //bean mocké

    @MockBean
    private TeacherService teacherService; //bean mocké


    Long userId;
    User userMock;
    Long teacherId;
    Teacher teacherMock;
    String jwt;


    @BeforeEach
    void beforeEach(){

      userId = 1L;
      userMock = new User();
      userMock.setId(userId);
      userMock.setEmail("yoga@studio.com");
      userMock.setFirstName("Alice");
      userMock.setLastName("tortellini");
      userMock.setPassword(passwordEncoder.encode("test!1234"));
      userMock.setAdmin(false);

      Mockito.when(userService.findById(userId)).thenReturn(userMock);
      Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));

      teacherId= 1L;
      teacherMock = new Teacher();
      teacherMock.setId(teacherId);
      teacherMock.setFirstName("Alice");
      teacherMock.setLastName("tortellini");

      Mockito.when(teacherService.findById(teacherId)).thenReturn(teacherMock);
      Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherMock));

      Authentication auth = new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234");
      Authentication authenticated = authenticationManager.authenticate(auth);
      jwt = jwtUtils.generateJwtToken(authenticated);



    }








    @Test
    void shouldFindAndReturnTeacherNumber1_IT() throws Exception {
      
      //login first before doing things ...



      mockMvc.perform(
        get("http://localhost:8080/api/teacher/{id}",teacherId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(teacherId))
        .andExpect(jsonPath("$.firstName").value("Alice"))
        .andExpect(jsonPath("$.lastName").value("tortellini"));
      
    }


    @Test
    void shouldNotFindTeacherAndReturn4xx_badId_IT() throws Exception {
      
      mockMvc.perform(
        get("http://localhost:8080/api/teacher/{id}",teacherId+1) //<-- +1 is important !!!
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$").doesNotExist());
      
    }


    @Test
    void shouldNotFindTeacherAndReturn4xx_badNumberFormat_IT() throws Exception {
      
      mockMvc.perform(
          get("http://localhost:8080/api/teacher/a") //<-- a is important !!!
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    void shouldFindAndReturnListOfTeacher_IT() throws Exception {
    

      Long teacherId1= 1L;
      Teacher teacherMock1 = new Teacher();
      teacherMock1.setId(teacherId1);
      teacherMock1.setFirstName("Alice");
      teacherMock1.setLastName("Tortellini");

      Long teacherId2= 2L;
      Teacher teacherMock2 = new Teacher();
      teacherMock2.setId(teacherId2);
      teacherMock2.setFirstName("Bob");
      teacherMock2.setLastName("Sinclair");

      Long teacherId3= 3L;
      Teacher teacherMock3 = new Teacher();
      teacherMock3.setId(teacherId3);
      teacherMock3.setFirstName("Conrad");
      teacherMock3.setLastName("Bad");

      List<Teacher> mockTeachers = List.of(
            teacherMock1,teacherMock2,teacherMock3
        );

      Mockito.when(teacherService.findAll()).thenReturn(mockTeachers);
      
      mockMvc.perform(
        get("http://localhost:8080/api/teacher")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"id\":1,\"firstName\":\"Alice\",\"lastName\":\"Tortellini\"},{\"id\":2,\"firstName\":\"Bob\",\"lastName\":\"Sinclair\"},{\"id\":3,\"firstName\":\"Conrad\",\"lastName\":\"Bad\"}]"));

      
    }
    
}
