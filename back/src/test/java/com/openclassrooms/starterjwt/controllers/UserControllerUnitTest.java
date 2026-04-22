package com.openclassrooms.starterjwt.controllers;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserControllerUnitTest {
    
    private UserService userService;
    private UserMapper userMapper;
    private UserRepository userRepository;
    private UserController userController;

    Long userId;
    String userIdString;
    Long userIdDeleted;
    User userMock;
    UserDto userDto;
    User userMockDeleted;
    Authentication auth;
    Authentication authenticated ;
    String jwt;

    @BeforeEach
    void beforeEach(){

      userService = Mockito.mock(UserService.class);
      userRepository = Mockito.mock(UserRepository.class);
      userMapper = Mockito.mock(UserMapper.class);

      userId = Constantes.LONG_UN;
      userMock = new User();
      userMock.setId(userId);
      userMock.setEmail(Constantes.STRING_EMAIL_YOGA);
      userMock.setFirstName(Constantes.STRING_BOB);
      userMock.setLastName(Constantes.STRING_BADINTER);
      userMock.setPassword(Constantes.STRING_PWD_CRYPTE);
      userMock.setAdmin(false);
      userMock.setCreatedAt(Constantes.localDateTime);
      userMock.setUpdatedAt(Constantes.localDateTime);

      userDto = new UserDto();
      userDto.setId(userId);
      userDto.setAdmin(false);
      userDto.setEmail(Constantes.STRING_EMAIL_YOGA);
      userDto.setFirstName(Constantes.STRING_BOB);
      userDto.setLastName(Constantes.STRING_BADINTER);
      userDto.setPassword(Constantes.STRING_PWD_CRYPTE);
      userDto.setCreatedAt(Constantes.localDateTime);
      userDto.setUpdatedAt(Constantes.localDateTime);

      Mockito.when(userService.findById(userId)).thenReturn(userMock);
      Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));
      Mockito.when(userMapper.toDto(userMock)).thenReturn(userDto);

      userController = new UserController(userService, userMapper);

    }

    @Test
    void shouldFindAndReturnUserNumber1() throws Exception {
      
      String bodyResponse = "UserDto(id=1, email=yoga@studio.com, lastName=BADINTER, firstName=Bob, admin=false, password=$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq, createdAt=2026-04-11T11:30:15, updatedAt=2026-04-11T11:30:15)";

      ResponseEntity<?> re = userController.findById("1");
      Assertions.assertEquals(HttpStatus.valueOf(200),re.getStatusCode());
      Assertions.assertEquals(bodyResponse,re.getBody().toString());

    }


    @Test
    void shouldNotFindAndReturn4xx_badId() throws Exception {
      
      ResponseEntity<?> re = userController.findById("2");
      Assertions.assertEquals(HttpStatus.valueOf(404),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());
      
    }

    @Test
    void shouldNotFindUserAndReturn4xx_badNumberFormat() throws Exception {
      
      ResponseEntity<?> re = userController.findById("a");
      Assertions.assertEquals(HttpStatus.valueOf(400),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());      
      
    }

    @Test
    void shouldNotFindAndNotDeleteUserAndReturn4xx_badId() throws Exception {

      ResponseEntity<?> re = userController.save("2");
      Assertions.assertEquals(HttpStatus.valueOf(404),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());
          
    }

    @Test
    void shouldNotFindAndNotDeleteUserAndReturn4xx_badNumberFormat() throws Exception {
      
      ResponseEntity<?> re = userController.save("a");
      Assertions.assertEquals(HttpStatus.valueOf(400),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());
      
    }

    @Test
    void shouldFindAndDeleteUserNumber1() throws Exception {

      UserDetails userDetails = Mockito.mock(UserDetails.class);
      Mockito.when(userDetails.getUsername()).thenReturn(Constantes.STRING_EMAIL_YOGA);

      Authentication authentication = Mockito.mock(Authentication.class);
      Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

      SecurityContext securityContext = Mockito.mock(SecurityContext.class);
      Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

      SecurityContextHolder.setContext(securityContext);

      ResponseEntity<?> re = userController.save("1");
      Assertions.assertEquals(HttpStatus.valueOf(200),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());
      
    }


    @Test
    void shouldNotDeleteUserAndReturn4xx_UserLoggedIsNotUserDeleted() throws Exception {
      
      //mock du security context
      UserDetails userDetails = Mockito.mock(UserDetails.class);
      Mockito.when(userDetails.getUsername()).thenReturn("fg@gmail.com");

      Authentication authentication = Mockito.mock(Authentication.class);
      Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

      SecurityContext securityContext = Mockito.mock(SecurityContext.class);
      Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

      SecurityContextHolder.setContext(securityContext);

      ResponseEntity<?> re = userController.save("1");
      Assertions.assertEquals(HttpStatus.valueOf(401),re.getStatusCode());
      Assertions.assertEquals(null,re.getBody());

    }    
 
  }
