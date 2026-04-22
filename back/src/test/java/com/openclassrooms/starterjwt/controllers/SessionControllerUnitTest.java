package com.openclassrooms.starterjwt.controllers;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;

//no anotations here, it is a real unit test (no need DB)
public class SessionControllerUnitTest{
  
    private TeacherRepository teacherRepository;

    private TeacherService teacherService; 

    private SessionRepository sessionRepository;

    private SessionMapper sessionMapper;

    private SessionDto sessionDto;

    private SessionService sessionService;

    private SessionController sessionController;
    
User userMock;
Long userId;
Long teacherId;
Teacher teacherMock;
Long sessionId;
Session sessionMock;

    @BeforeEach
    void beforeEach(){

      teacherService = Mockito.mock(TeacherService.class);

      teacherRepository = Mockito.mock(TeacherRepository.class);

      sessionMapper = Mockito.mock(SessionMapper.class);

      sessionId = Constantes.LONG_UN;

      sessionDto = new SessionDto();

      sessionDto.setId(sessionId);
      sessionDto.setName(Constantes.STRING_PILATE);
      sessionDto.setDescription(Constantes.STRING_VENEZ);
      sessionDto.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionDto.setTeacher_id(Constantes.LONG_UN);
      sessionDto.setUsers(Constantes.EMPTY_LONG_LIST);
      sessionDto.setUpdatedAt(Constantes.localDateTime);
      sessionDto.setCreatedAt(Constantes.localDateTime);

      sessionService = Mockito.mock(SessionService.class);

      sessionRepository = Mockito.mock(SessionRepository.class);

      sessionController = new SessionController(sessionService, sessionMapper);

      teacherId= 1L;
      teacherMock = Mockito.mock( Teacher.class );
      teacherMock.setId(teacherId);
      teacherMock.setFirstName(Constantes.STRING_ALICE);
      teacherMock.setLastName(Constantes.STRING_TAGLIONI);

      Mockito.when(teacherService.findById(teacherId)).thenReturn(teacherMock);
      Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherMock));

      sessionId = Constantes.LONG_UN;
      sessionMock = Mockito.mock(Session.class);
      sessionMock.setId(sessionId);
      sessionMock.setDescription(Constantes.STRING_VENEZ);
      sessionMock.setName(Constantes.STRING_PILATE);
      sessionMock.setTeacher(teacherMock);
      sessionMock.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionMock.setCreatedAt(Constantes.localDateTime);
      sessionMock.setUpdatedAt(Constantes.localDateTime);
      sessionMock.setUsers(Constantes.EMPTY_USER_LIST);

      Mockito.when(sessionService.getById(sessionId)).thenReturn(sessionMock);
      Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionMock));

      Mockito.when(sessionMapper.toDto(sessionMock)).thenReturn(sessionDto);
      Mockito.when(sessionMapper.toEntity(sessionDto)).thenReturn(sessionMock);
    }

    @Test
    void shouldFindAndReturnSessionNumber1() throws Exception {

      String bodyResponse = "SessionDto(id=1, name=pilate, date=Wed Jan 28 00:00:00 CET 2026, teacher_id=1, description=Venez nombreux à mon cours de pilates..., users=[], createdAt=2026-04-11T11:30:15, updatedAt=2026-04-11T11:30:15)";

      ResponseEntity<?> re = sessionController.findById("1");
      Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
      Assertions.assertEquals(bodyResponse,re.getBody().toString());

    }

    @Test
    void shouldNotFindSessionAndReturn4xx_badId() throws Exception {

      ResponseEntity<?> re = sessionController.findById("2");
      Assertions.assertEquals(HttpStatus.valueOf(404), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }


    @Test
    void shouldNotFindSesssionAndReturn4xx_badNumberFormat() throws Exception {
      
      ResponseEntity<?> re = sessionController.findById("2");
      Assertions.assertEquals(HttpStatus.valueOf(404), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldFindSessionAndDeleteSession() throws Exception {
      
      ResponseEntity<?> re = sessionController.save("1");
      Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldNotFindSessionAndReturn4xx_BadNumberRequest() throws Exception {
    
      ResponseEntity<?> re = sessionController.save("a");
      Assertions.assertEquals(HttpStatus.valueOf(400), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldNotFindSessionAndReturn4xx_NotFound() throws Exception {
      
      ResponseEntity<?> re = sessionController.save("2");
      Assertions.assertEquals(HttpStatus.valueOf(404), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldParticipateOK_IT() throws Exception {
      
      ResponseEntity<?> re = sessionController.participate("1","1");
      Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldParticipateButReturn4xx_NotFound() throws Exception {

      ResponseEntity<?> re = sessionController.participate("a","b");
      Assertions.assertEquals(HttpStatus.valueOf(400), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldUnParticipateOK() throws Exception {
      
      ResponseEntity<?> re = sessionController.noLongerParticipate("1","1");
      Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldUnParticipateButReturn4xx_NotFound() throws Exception {
      
      ResponseEntity<?> re = sessionController.noLongerParticipate("a","b");
      Assertions.assertEquals(HttpStatus.valueOf(400), re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldFindAndReturnListOfSession() throws Exception {
      
      //LocalDateTime localDateTimeNow = LocalDateTime.now();
      userId = Constantes.LONG_UN;
      userMock = new User();
      userMock.setId(userId);
      userMock.setEmail(Constantes.STRING_EMAIL_YOGA);
      userMock.setFirstName(Constantes.STRING_BOB);
      userMock.setLastName(Constantes.STRING_BADINTER);
      userMock.setPassword(Constantes.STRING_PWD_CRYPTE);
      userMock.setAdmin(false);

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
      sessionMock2.setCreatedAt(Constantes.localDateTime);
      sessionMock2.setUpdatedAt(Constantes.localDateTime);

      Long sessionId3= 3L;
      Session sessionMock3 = new Session(sessionId3,"impressionnant",new GregorianCalendar(2025,12,28).getTime(),"reflexo",teacherMock,List.of(userMock),Constantes.localDateTime,Constantes.localDateTime);
      sessionMock3.setId(sessionId3);
      sessionMock3.setName("impressionnant");
      sessionMock3.setDescription("reflexo");
      sessionMock3.setTeacher(teacherMock);
      sessionMock3.setDate(new GregorianCalendar(2025,12,28).getTime());
      sessionMock3.setUsers(List.of(userMock));

      List<Session> mockSessions = List.of(
            sessionMock1,sessionMock2,sessionMock3
        );

      
      
      SessionDto sessionDto1 = new SessionDto(sessionId1, "pilate", new GregorianCalendar(2025,12,28).getTime(), teacherMock.getId(), "super", null, null, null);
      SessionDto sessionDto2 = new SessionDto(sessionId2, "genial", new GregorianCalendar(2025,12,28).getTime() , teacherMock.getId(), "massage", null, Constantes.localDateTime, Constantes.localDateTime);
      SessionDto sessionDto3 = new SessionDto(sessionId3, "impressionnant", new GregorianCalendar(2025,12,28).getTime(), teacherMock.getId(), "reflexo", List.of(1L,2L,3L), null, null);

      List<SessionDto> mockSessionsDto = List.of(
            sessionDto1,sessionDto2,sessionDto3
        );

      Mockito.when(sessionService.findAll()).thenReturn(mockSessions);
      Mockito.when(sessionMapper.toDto(mockSessions)).thenReturn(mockSessionsDto);
      //123456789112345678921234567
      //String ldt = "2026-04-11T11:30:15";//localDateTimeNow.toString().substring(0,Math.min(27,localDateTimeNow.toString().length()));
                    //2026-04-05T13:36:03.979167600
      //ldt = ldt + addon;      //2026-04-02T22:42:53.8604369
      //String verif1 = "{\"id\":1,\"name\":\"pilate\",\"description\":\"super\",\"date\":\"2026-01-27T23:00:00.000+00:00\",\"teacher_id\":1,\"users\":[],\"createdAt\":null,\"updatedAt\":null}";
      //String verif2 = "{\"id\":2,\"name\":\"genial\",\"description\":\"massage\",\"date\":\"2026-01-27T23:00:00.000+00:00\",\"teacher_id\":1,\"users\":[],\"createdAt\":\""+ldt+"\",\"updatedAt\":\""+ldt+"\"}";
      //String verif3 = "{\"id\":3,\"name\":\"impressionnant\",\"description\":\"reflexo\",\"date\":\"2026-01-27T23:00:00.000+00:00\",\"teacher_id\":1,\"users\":[1],\"createdAt\":\""+ldt+"\",\"updatedAt\":\""+ldt+"\"}";
      
      String v1 = "SessionDto(id=1, name=pilate, date=Wed Jan 28 00:00:00 CET 2026, teacher_id=0, description=super, users=null, createdAt=null, updatedAt=null)";
      String v2 = "SessionDto(id=2, name=genial, date=Wed Jan 28 00:00:00 CET 2026, teacher_id=0, description=massage, users=null, createdAt=2026-04-11T11:30:15, updatedAt=2026-04-11T11:30:15)";
      String v3 = "SessionDto(id=3, name=impressionnant, date=Wed Jan 28 00:00:00 CET 2026, teacher_id=0, description=reflexo, users=[1, 2, 3], createdAt=null, updatedAt=null)";

      /*
       [
       {"id":1,"name":"pilate","date":"2026-01-27T23:00:00.000+00:00","teacher_id":1,"description":"super","users":[],"createdAt":null,"updatedAt":null},
       {"id":2,"name":"genial","date":"2026-01-27T23:00:00.000+00:00","teacher_id":1,"description":"massage","users":[],"createdAt":null,"updatedAt":null},
       {"id":3,"name":"impressionnant","date":"2026-01-27T23:00:00.000+00:00","teacher_id":1,"description":"reflexo","users":[1],"createdAt":null,"updatedAt":null}
       ]
      */

      String requestBody = "[" + v1 + ", " + v2 + ", " + v3 + "]"; 

      ResponseEntity<?> re = sessionController.findAll();
      Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
      Assertions.assertEquals(requestBody,re.getBody().toString());
      
    }

  @Test
  void shouldCreateSessionAndReturnSession() throws Exception {

    Mockito.when(sessionService.create(sessionMock)).thenReturn(sessionMock);
    String bodyResponse = "SessionDto(id=1, name=pilate, date=Wed Jan 28 00:00:00 CET 2026, teacher_id=1, description=Venez nombreux à mon cours de pilates..., users=[], createdAt=2026-04-11T11:30:15, updatedAt=2026-04-11T11:30:15)";
    ResponseEntity<?> re = sessionController.create(sessionDto);
    Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
    Assertions.assertEquals(bodyResponse,re.getBody().toString());

  }

  @Test
  void shouldUpdateSessionAndReturnSession() throws Exception {
    
    Mockito.when(sessionService.update(sessionId,sessionMock)).thenReturn(sessionMock);
    String bodyResponse = "SessionDto(id=1, name=pilate, date=Wed Jan 28 00:00:00 CET 2026, teacher_id=1, description=Venez nombreux à mon cours de pilates..., users=[], createdAt=2026-04-11T11:30:15, updatedAt=2026-04-11T11:30:15)";
    ResponseEntity<?> re = sessionController.update("1",sessionDto);
    Assertions.assertEquals(HttpStatus.valueOf(200), re.getStatusCode());
    Assertions.assertEquals(bodyResponse,re.getBody().toString());

  }


  @Test
  void shouldSaveSessionAndReturn4xx_BadNumberRequest() throws Exception {

    ResponseEntity<?> re = sessionController.update("a", sessionDto);
    Assertions.assertEquals(HttpStatus.valueOf(400), re.getStatusCode());
    Assertions.assertEquals(null,re.getBody());

  }
}