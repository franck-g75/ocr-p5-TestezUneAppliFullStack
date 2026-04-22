package com.openclassrooms.starterjwt.controllers;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

public class TeacherControllerUnitTest {
    
    private UserRepository userRepository;
    
    private UserService userService;
    
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    private TeacherMapper teacherMapper;

    private TeacherController teacherController;

    Long userId;
    User userMock;
    Long teacherId;
    Teacher teacherMock;
    TeacherDto teacherDto;

    @BeforeEach
    void beforeEach(){

      userService = Mockito.mock(UserService.class);
      userRepository = Mockito.mock(UserRepository.class);
      teacherService = Mockito.mock(TeacherService.class);
      teacherRepository = Mockito.mock(TeacherRepository.class);
      teacherMapper = Mockito.mock(TeacherMapper.class);


      userId = Constantes.LONG_UN;
      userMock = new User();
      userMock.setId(userId);
      userMock.setEmail(Constantes.STRING_EMAIL_YOGA);
      userMock.setFirstName(Constantes.STRING_BOB);
      userMock.setLastName(Constantes.STRING_BADINTER);
      userMock.setPassword(Constantes.STRING_PWD_CRYPTE);
      userMock.setAdmin(false);

      Mockito.when(userService.findById(userId)).thenReturn(userMock);
      Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));

      teacherId= Constantes.LONG_UN;
      teacherMock = new Teacher();
      teacherMock.setId(teacherId);
      teacherMock.setFirstName(Constantes.STRING_ALICE);
      teacherMock.setLastName(Constantes.STRING_TAGLIONI);

      Mockito.when(teacherService.findById(teacherId)).thenReturn(teacherMock);
      Mockito.when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherMock));

      teacherDto = new TeacherDto();
      teacherDto.setId(Constantes.LONG_UN);
      teacherDto.setLastName(Constantes.STRING_TAGLIONI);
      teacherDto.setFirstName(Constantes.STRING_ALICE);
      teacherDto.setCreatedAt(Constantes.localDateTime);
      teacherDto.setUpdatedAt(Constantes.localDateTime);

      Mockito.when(teacherMapper.toDto(teacherMock)).thenReturn(teacherDto);

      teacherController = new TeacherController(teacherService, teacherMapper);

    }

    @Test
    void shouldFindAndReturnTeacherNumber1() throws Exception {
      
      String bodyResponse = "TeacherDto(id=1, lastName=TAGLIONI, firstName=Alice, createdAt=2026-04-11T11:30:15, updatedAt=2026-04-11T11:30:15)";

      ResponseEntity<?> re = teacherController.findById("1");
      Assertions.assertEquals(HttpStatus.valueOf(200),re.getStatusCode());
      Assertions.assertEquals(bodyResponse,re.getBody().toString());

    }

    @Test
    void shouldNotFindTeacherAndReturn404() throws Exception {
      
      ResponseEntity<?> re = teacherController.findById("2");
      Assertions.assertEquals(HttpStatus.valueOf(404),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }


    @Test
    void shouldNotFindTeacherAndReturn4xx_badNumberFormat_IT() throws Exception {
      
      ResponseEntity<?> re = teacherController.findById("a");
      Assertions.assertEquals(HttpStatus.valueOf(400),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }

    @Test
    void shouldFindAndReturnListOfTeacher_IT() throws Exception {
    
      Long teacherId1= Constantes.LONG_UN;
      Teacher teacherMock1 = new Teacher();
      teacherMock1.setId(teacherId1);
      teacherMock1.setFirstName(Constantes.STRING_ALICE);
      teacherMock1.setLastName(Constantes.STRING_TAGLIONI);

      Long teacherId2= Constantes.LONG_DEUX;
      Teacher teacherMock2 = new Teacher();
      teacherMock2.setId(teacherId2);
      teacherMock2.setFirstName(Constantes.STRING_BOB);
      teacherMock2.setLastName(Constantes.STRING_BADINTER);

      Long teacherId3= Constantes.LONG_TROIS;
      Teacher teacherMock3 = new Teacher();
      teacherMock3.setId(teacherId3);
      teacherMock3.setFirstName(Constantes.STRING_FRANCK);
      teacherMock3.setLastName(Constantes.STRING_GUINDEUIL);

      List<Teacher> mockTeachers = List.of(
            teacherMock1,teacherMock2,teacherMock3
        );

      TeacherDto teacherDto1 = new TeacherDto();
      teacherDto1.setId(teacherId1);
      teacherDto1.setFirstName(Constantes.STRING_ALICE);
      teacherDto1.setLastName(Constantes.STRING_TAGLIONI);

      TeacherDto teacherDto2 = new TeacherDto();
      teacherDto2.setId(teacherId2);
      teacherDto2.setFirstName(Constantes.STRING_BOB);
      teacherDto2.setLastName(Constantes.STRING_BADINTER);

      TeacherDto teacherDto3 = new TeacherDto();
      teacherDto3.setId(teacherId2);
      teacherDto3.setFirstName(Constantes.STRING_FRANCK);
      teacherDto3.setLastName(Constantes.STRING_GUINDEUIL);

      List<TeacherDto> teacherDtoList = List.of( teacherDto1, teacherDto2, teacherDto3 );

      Mockito.when(teacherService.findAll()).thenReturn(mockTeachers);
      Mockito.when(teacherMapper.toDto(mockTeachers)).thenReturn(teacherDtoList);

      String requestBody = "[TeacherDto(id=1, lastName=TAGLIONI, firstName=Alice, createdAt=null, updatedAt=null), TeacherDto(id=2, lastName=BADINTER, firstName=Bob, createdAt=null, updatedAt=null), TeacherDto(id=2, lastName=GUINDEUIL, firstName=Franck, createdAt=null, updatedAt=null)]"; 

      ResponseEntity<?> re = teacherController.findAll();
      Assertions.assertEquals(HttpStatus.valueOf(200),re.getStatusCode());
      Assertions.assertEquals(requestBody,re.getBody().toString());
            
    }

}
