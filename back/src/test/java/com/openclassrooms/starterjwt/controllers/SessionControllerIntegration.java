package com.openclassrooms.starterjwt.controllers;

//912 ligne avant 433 apres

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootTest           //doesn't work when database is down
//@AutoConfigureMockMvc
public class SessionControllerIntegration<JpaMappingContext> {
  
    @MockBean
    private UserRepository userRepository; //bean mocké

    @MockBean
    private UserService userService; //bean mocké

    @MockBean
    private TeacherRepository teacherRepository; //bean mocké

    @MockBean
    private TeacherService teacherService; //bean mocké

    @MockBean
    private SessionRepository sessionRepository; //bean mocké

    @MockBean
    private SessionService sessionService; //bean mocké

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    Long userId;
    User userMock;
    Long teacherId;
    Teacher teacherMock;
    Long sessionId;
    Session sessionMock;
    String jwt;
    


    @BeforeEach
    void beforeEach(){

      //mockMvc = MockMvcBuilders.standaloneSetup(sessionController).build();

      userId= 1L;
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

      sessionId = 1L;
      sessionMock = new Session();
      sessionMock.setId(sessionId);
      sessionMock.setDescription("super");
      sessionMock.setName("pilate");
      sessionMock.setTeacher(teacherMock);
      sessionMock.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionMock.setUsers(List.of(userMock));

      Mockito.when(sessionService.getById(sessionId)).thenReturn(sessionMock);
      Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionMock));

      Authentication auth = new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234");
      Authentication authenticated = authenticationManager.authenticate(auth);
      jwt = jwtUtils.generateJwtToken(authenticated);

    }


    @Test
    void shouldFindAndReturnSessionNumber1() throws Exception {
      
      mockMvc.perform(
        get("http://localhost:8080/api/session/{id}",sessionId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(sessionId))
        .andExpect(jsonPath("$.name").value("pilate"))
        .andExpect(jsonPath("$.description").value("super"))
        .andExpect(jsonPath("teacher_id").value(1))
        .andExpect(jsonPath("$.users").isArray());

    }

    @Test
    void shouldNotFindSessionAndReturn4xx_badId() throws Exception {

      mockMvc.perform(
        get("http://localhost:8080/api/session/{id}",sessionId+1)     //<-- +1 is important !!!
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$").doesNotExist());
     
    }


    @Test
    void shouldNotFindTeacherAndReturn4xx_badNumberFormat() throws Exception {
      
      mockMvc.perform(
          get("http://localhost:8080/api/session/a") //<-- a is important !!!
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
          .andExpect(status().is4xxClientError())
          .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    void shouldFindSessionAndDeleteSession() throws Exception {
      
      mockMvc.perform(
        delete("http://localhost:8080/api/session/{id}", sessionId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
    }



  @Test
  void shouldNotFindSessionAndReturn4xx_BadNumberRequest() throws Exception {
    
    mockMvc.perform(
      delete("http://localhost:8080/api/session/a")
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      )
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$").doesNotExist());
  }


  @Test
  void shouldNotFindSessionAndReturn4xx_NotFound_IT() throws Exception {
    
    mockMvc.perform(
      delete("http://localhost:8080/api/session/{id}", sessionId+1)
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      )
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$").doesNotExist());
  }


  @Test
  void shouldParticipateOK() throws Exception {
    
    mockMvc.perform(
      post("http://localhost:8080/api/session/{id}/participate/{userId}",sessionId,userId)
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
       ).andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }


  @Test
  void shouldParticipateButReturn4xx_NotFound() throws Exception {
    
    mockMvc.perform(
      post("http://localhost:8080/api/session/a/participate/b")
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      )
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$").doesNotExist());
  }


  @Test
  void shouldUnParticipateOK() throws Exception {
    
    mockMvc.perform(
      delete("http://localhost:8080/api/session/{id}/participate/{userId}",sessionId,userId)
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
       ).andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }



  @Test
  void shouldUnParticipateButReturn4xx_NotFound() throws Exception {
    
    mockMvc.perform(
      delete("http://localhost:8080/api/session/a/participate/b")
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      )
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$").doesNotExist());
  }





    @Test
    void shouldFindAndReturnListOfSession() throws Exception {
      
      //login first before doing things ...

      LocalDateTime localDateTimeNow = LocalDateTime.now();

      Long sessionId1= 1L;
      Session sessionMock1 = new Session();
      sessionMock1.setId(sessionId1);
      sessionMock1.setDescription("super");
      sessionMock1.setName("pilate");
      sessionMock1.setTeacher(teacherMock);
      sessionMock1.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionMock1.setUsers(null);

      Long sessionId2= 2L;
      Session sessionMock2 = new Session();
      sessionMock2.setId(sessionId2);
      sessionMock2.setName("genial");
      sessionMock2.setDescription("massage");
      sessionMock2.setTeacher(teacherMock);
      sessionMock2.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionMock2.setUsers(null);
      sessionMock2.setCreatedAt(localDateTimeNow);
      sessionMock2.setUpdatedAt(localDateTimeNow);

      Long sessionId3= 3L;
      Session sessionMock3 = new Session(sessionId3,"impressionnant",new GregorianCalendar(2025,12,28).getTime(),"reflexo",teacherMock,List.of(userMock),localDateTimeNow,localDateTimeNow);
      sessionMock3.setId(sessionId3);
      sessionMock3.setName("impressionnant");
      sessionMock3.setDescription("reflexo");
      sessionMock3.setTeacher(teacherMock);
      sessionMock3.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionMock3.setUsers(List.of(userMock));

      List<Session> mockSessions = List.of(
            sessionMock1,sessionMock2,sessionMock3
        );

      Mockito.when(sessionService.findAll()).thenReturn(mockSessions);
      
      Authentication auth = new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234");
      Authentication authenticated = authenticationManager.authenticate(auth);
      String jwt = jwtUtils.generateJwtToken(authenticated);       //123456789112345678921234567
      String ldt = localDateTimeNow.toString().substring(0,Math.min(27,localDateTimeNow.toString().length()));
      String addon = "";
      for (int i = ldt.length(); i <= 27; i++) {
        addon.concat("0"); 
                              //12345678911234567892123456789
      }                       //2026-04-05T13:36:03.979167600
      ldt = ldt + addon;      //2026-04-02T22:42:53.8604369
      String verif1 = "{\"id\":1,\"name\":\"pilate\",\"description\":\"super\",\"date\":\"2026-01-27T23:00:00.000+00:00\",\"teacher_id\":1,\"users\":[],\"createdAt\":null,\"updatedAt\":null}";
      String verif2 = "{\"id\":2,\"name\":\"genial\",\"description\":\"massage\",\"date\":\"2026-01-27T23:00:00.000+00:00\",\"teacher_id\":1,\"users\":[],\"createdAt\":\""+ldt+"\",\"updatedAt\":\""+ldt+"\"}";
      String verif3 = "{\"id\":3,\"name\":\"impressionnant\",\"description\":\"reflexo\",\"date\":\"2026-01-27T23:00:00.000+00:00\",\"teacher_id\":1,\"users\":[1],\"createdAt\":\""+ldt+"\",\"updatedAt\":\""+ldt+"\"}";

    
      // [
      // {"id":1,"name":"pilate","date":"2026-01-27T23:00:00.000+00:00","teacher_id":1,"description":"super","users":[],"createdAt":null,"updatedAt":null},
      // {"id":2,"name":"genial","date":"2026-01-27T23:00:00.000+00:00","teacher_id":1,"description":"massage","users":[],"createdAt":null,"updatedAt":null},
      // {"id":3,"name":"impressionnant","date":"2026-01-27T23:00:00.000+00:00","teacher_id":1,"description":"reflexo","users":[1],"createdAt":null,"updatedAt":null}
      // ]
      
      mockMvc.perform(
        get("http://localhost:8080/api/session")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[" + verif1 + "," + verif2 +"," + verif3 + "]"));
    }
    




  @Test
  void shouldCreateSessionAndReturnSession() throws Exception {
    
    SessionDto sessionDto = sessionMapper.toDto(sessionMock);
    ObjectMapper mapper = new ObjectMapper();
    byte[] json = new byte[0];
    json = mapper.writeValueAsBytes(sessionDto);

    //log.warn("json : " + json.toString());
    //log.warn("dto : " + sessionDto.toString());

    Mockito.when(sessionService.create(sessionMock)).thenReturn(sessionMock);

    mockMvc.perform(
      post("http://localhost:8080/api/session/")
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      .contentType("application/json")
      .content(json))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(sessionId))
      .andExpect(jsonPath("$.name").value("pilate"))
      .andExpect(jsonPath("$.description").value("super"))
      .andExpect(jsonPath("teacher_id").value(1))
      .andExpect(jsonPath("$.users").isArray());

  }

  @Test
  void shouldSaveSessionAndReturnSession() throws Exception {
    
    Session sessionMockBis = new Session();
    sessionMockBis.setId(sessionId);
    sessionMockBis.setDescription("superBis");
    sessionMockBis.setName("pilateBis");
    sessionMockBis.setTeacher(teacherMock);
    sessionMockBis.setDate(new GregorianCalendar(2025,12,28).getTime());
    sessionMockBis.setUsers(List.of(userMock));

    SessionDto sessionDto = sessionMapper.toDto(sessionMock);
    ObjectMapper mapper = new ObjectMapper();
    byte[] json = new byte[0];
    json = mapper.writeValueAsBytes(sessionDto);

    Mockito.when(sessionService.update(sessionId,sessionMockBis)).thenReturn(sessionMockBis);

    mockMvc.perform(
      put("http://localhost:8080/api/session/{id}", sessionId)
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      .contentType("application/json")
      .content(json))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(sessionId))
      .andExpect(jsonPath("$.name").value("pilateBis"))
      .andExpect(jsonPath("$.description").value("superBis"))
      .andExpect(jsonPath("teacher_id").value(1))
      .andExpect(jsonPath("$.users").isArray());

  }

  @Test
  void shouldSaveSessionAndReturn4xx_BadNumberRequest() throws Exception {

    Session sessionMockBis = new Session();
    sessionMockBis.setId(sessionId);
    sessionMockBis.setDescription("superBis");
    sessionMockBis.setName("pilateBis");
    sessionMockBis.setTeacher(teacherMock);
    sessionMockBis.setDate(new GregorianCalendar(2025,12,28).getTime());
    sessionMockBis.setUsers(List.of(userMock));

    SessionDto sessionDto = sessionMapper.toDto(sessionMock);
    ObjectMapper mapper = new ObjectMapper();
    byte[] json = new byte[0];
    json = mapper.writeValueAsBytes(sessionDto);

    Mockito.when(sessionService.update(sessionId,sessionMockBis)).thenReturn(sessionMockBis);

    mockMvc.perform(
      put("http://localhost:8080/api/session/a")
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt).accept(MediaType.APPLICATION_JSON).contentType("jwt")
      .contentType("application/json")
      .content(json))
      .andExpect(status().is4xxClientError())
      .andExpect(jsonPath("$").doesNotExist());

  }


}