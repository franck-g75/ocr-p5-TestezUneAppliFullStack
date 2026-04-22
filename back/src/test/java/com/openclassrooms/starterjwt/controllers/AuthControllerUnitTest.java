package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthControllerUnitTest {
    
    private AuthController authController;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;
    private UserRepository userRepository; 
    private UserService userService; 

    Long userId;
    User userMock;
    String jwt;
    LoginRequest loginRequest;
    SignupRequest signupRequest;

    @BeforeEach
    void beforeEach(){

        userId = Constantes.LONG_UN;
        userMock = new User();
        userMock.setId(userId);
        userMock.setEmail(Constantes.STRING_EMAIL_YOGA);
        userMock.setFirstName(Constantes.STRING_BOB);
        userMock.setLastName(Constantes.STRING_BADINTER);
        userMock.setPassword(Constantes.STRING_PWD_CRYPTE);
        userMock.setAdmin(true);

        loginRequest = new LoginRequest();
        loginRequest.setPassword(Constantes.STRING_PWD_NOT_CRYPTE);
        loginRequest.setEmail(Constantes.STRING_EMAIL_YOGA);

        signupRequest = new SignupRequest();
        signupRequest.setPassword(Constantes.STRING_PWD_NOT_CRYPTE);
        signupRequest.setEmail(Constantes.STRING_EMAIL_YOGA);
        signupRequest.setFirstName(Constantes.STRING_BOB);
        signupRequest.setLastName(Constantes.STRING_BADINTER);

        userRepository = Mockito.mock(UserRepository.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        jwtUtils = Mockito.mock(JwtUtils.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = Mockito.mock(UserService.class);

        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }
    
    @Test
    void shouldNotRegister_EmailAlreadyTaken() throws Exception {
        
        Mockito.when(userRepository.existsByEmail(Constantes.STRING_EMAIL_YOGA)).thenReturn(true);

        String bodyResponse = "Error: Email is already taken!";

        ResponseEntity<?> re = authController.registerUser(signupRequest);
        Assertions.assertEquals(HttpStatus.valueOf(400),re.getStatusCode());
        Assertions.assertEquals(bodyResponse,((MessageResponse)re.getBody()).getMessage());

    }

    @Test
    void shouldRegisterWithoutError() throws Exception {
        
        Mockito.when(passwordEncoder.encode(Constantes.STRING_PWD_NOT_CRYPTE)).thenReturn(Constantes.STRING_PWD_CRYPTE);
        Mockito.when(userRepository.existsByEmail(Constantes.STRING_EMAIL_YOGA)).thenReturn(false);
        
        String bodyResponse = "User registered successfully!";
        
        ResponseEntity<?> re = authController.registerUser(signupRequest);
        Assertions.assertEquals(HttpStatus.valueOf(200),re.getStatusCode());
        Assertions.assertEquals(bodyResponse,((MessageResponse)re.getBody()).getMessage());

    }

    @Test
    void shouldLoginSuccessfully_IT() throws Exception {
        
        //GIVEN

        Authentication authentication = mock(Authentication.class);

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, Constantes.STRING_EMAIL_YOGA, Constantes.STRING_BOB, Constantes.STRING_BADINTER,true, Constantes.STRING_PWD_NOT_CRYPTE);

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);    

        Mockito.when(authentication.getPrincipal()).thenReturn((userDetails));

        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("theJWT");

        Mockito.when(userService.findById(userId)).thenReturn(userMock);
        
        Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));
    
        //WHEN
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        //THEN
        assertEquals(200, response.getStatusCodeValue());

        JwtResponse jwtResponse = (JwtResponse)response.getBody();

        assertEquals("theJWT", jwtResponse.getToken());
        assertEquals(Constantes.STRING_EMAIL_YOGA, jwtResponse.getUsername());
        assertEquals(Constantes.STRING_BOB, jwtResponse.getFirstName());
        assertEquals(Constantes.STRING_BADINTER, jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());

        verify(authenticationManager).authenticate(any());
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepository).findByEmail(Constantes.STRING_EMAIL_YOGA);

    }

    @Test
    void shouldLoginSuccessfully_UserNotFoundInDB_IT() throws Exception {
        
        //GIVEN

        Authentication authentication = mock(Authentication.class);

        UserDetailsImpl userDetails = new UserDetailsImpl(userId, Constantes.STRING_EMAIL_YOGA, Constantes.STRING_BOB, Constantes.STRING_BADINTER,true, Constantes.STRING_PWD_NOT_CRYPTE);

        Mockito.when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);    

        Mockito.when(authentication.getPrincipal()).thenReturn((userDetails));

        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("theJWT");

        Mockito.when(userService.findById(userId)).thenReturn(userMock);
        
        Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.empty());  //not found
    
        //WHEN
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        //THEN
        assertEquals(200, response.getStatusCodeValue());

        JwtResponse jwtResponse = (JwtResponse)response.getBody();

        assertEquals("theJWT", jwtResponse.getToken());
        assertEquals(Constantes.STRING_EMAIL_YOGA, jwtResponse.getUsername());
        assertEquals(Constantes.STRING_BOB, jwtResponse.getFirstName());
        assertEquals(Constantes.STRING_BADINTER, jwtResponse.getLastName());
        assertFalse(jwtResponse.getAdmin());                                //not admin

        verify(authenticationManager).authenticate(any());
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepository).findByEmail(Constantes.STRING_EMAIL_YOGA);

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
