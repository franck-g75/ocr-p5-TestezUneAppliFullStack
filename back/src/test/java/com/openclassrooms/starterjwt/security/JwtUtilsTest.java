package com.openclassrooms.starterjwt.security;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.jsonwebtoken.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class JwtUtilsTest {
    
    @Autowired
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;      //for token generation

    @Mock
    private UserDetailsImpl userDetails;    //for token generation

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Test
	public void validateJwtToken_ShouldReturnSignatureExceptions(CapturedOutput output) {
        //ARRANGE GIVEN     ( + error in signature)
        String token = Jwts.builder()
        .setSubject("alice.tortellini@truc.com")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret + "error")
        .compact();

        //ACT WHEN
        jwtUtils.validateJwtToken(token);        
        
        //ASSERT VERIFY
        assertThat(output).contains("Invalid JWT signature:");
    }

    @Test
	public void validateJwtToken_ShouldReturnMalformedJwtException(CapturedOutput output) {

        //ARRANGE GIVEN     (isssuedAt omited)
        String token = Jwts.builder()
        .setSubject("alice.tortellini@truc.com")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .toString();
        //.compact();

        //ACT WHEN
        jwtUtils.validateJwtToken(token);

        //ASSERT VERIFY
        assertThat(output).contains("Invalid JWT token:");
        
    }

    @Test
	public void validateJwtToken_ShouldReturnExpiredJwtException (CapturedOutput output) {

        //ARRANGE GIVEN   (- expiration not + expiration delay)
        String token = Jwts.builder()
        .setSubject("alice.tortellini@truc.com")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() - jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

        //ACT WHEN
        jwtUtils.validateJwtToken(token);

        //ASSERT VERIFY 
        assertThat(output).contains("JWT token is expired:");
    }

    @Test
	public void validateJwtToken_ShouldReturnUnsupportedJwtException  (CapturedOutput output) {

        //ARRANGE GIVEN     (RS512 instead of HS512)
        String token = Jwts.builder()
        .setSubject("alice.tortellini@truc.com")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        //.signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact();

        //ACT WHEN
        jwtUtils.validateJwtToken(token);

        //ASSERT VERIFY 
        assertThat(output).contains("JWT token is unsupported:");
    }

    @Test
	public void validateJwtToken_ShouldReturnIllegalArgumentException   (CapturedOutput output) {

        //ARRANGE GIVEN     (empty string)
        String token = "";

        //ACT WHEN
        jwtUtils.validateJwtToken(token);

        //ASSERT VERIFY 
        assertThat(output).contains("JWT claims string is empty:");
    }

    @Test
    public void getUserNameFromJwtToken_shouldReturnUsername(){
        //ARRANGE GIVEN    
         String token = Jwts.builder()
        .setSubject("alice.tortellini@truc.com")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

        //ACT WHEN
        String userName = jwtUtils.getUserNameFromJwtToken(token);

        //ASSERT VERIFY 
        Assertions.assertEquals(userName,"alice.tortellini@truc.com" );

    }

    @Test
    public void returnTrue_TokenValid() {

        //jwtSecret : private key in properties

        String token = Jwts.builder().setSubject("yoga@studio.com").signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils,"jwtSecret", jwtSecret);
        
        boolean result = jwtUtils.validateJwtToken(token);

        Assertions.assertTrue(result);

    }

    @Test
    public void returnFalse_TokenInvalid() {

        //jwtSecret : private key in properties

        //String token = Jwts.builder().setSubject("yoga@studio.com").signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils,"jwtSecret", jwtSecret);
        
        boolean result = jwtUtils.validateJwtToken("coucou token invalide");

        Assertions.assertFalse(result);

    }

    @Test
    public void returnValidToken(){

        //GIVEN ARRANGE
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(userDetails.getUsername()).thenReturn(Constantes.STRING_EMAIL_YOGA);

        //WHEN ACT
        String token = jwtUtils.generateJwtToken(authentication);
        
        //THEN ASSERT
        Assertions.assertNotNull(token);

        //decrypt the token with jwtSecret key
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        
        assertEquals(Constantes.STRING_EMAIL_YOGA, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());

    }



}
