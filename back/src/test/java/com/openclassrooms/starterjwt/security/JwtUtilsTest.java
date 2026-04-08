package com.openclassrooms.starterjwt.security;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

import static org.assertj.core.api.Assertions.*;

import io.jsonwebtoken.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
public class JwtUtilsTest {
    
    @Autowired
    private JwtUtils jwtUtils;

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
        boolean b = jwtUtils.validateJwtToken(token);        
        
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
        boolean b = jwtUtils.validateJwtToken(token);

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
        boolean b = jwtUtils.validateJwtToken(token);

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
        boolean b = jwtUtils.validateJwtToken(token);

        //ASSERT VERIFY 
        assertThat(output).contains("JWT token is unsupported:");
    }

    @Test
	public void validateJwtToken_ShouldReturnIllegalArgumentException   (CapturedOutput output) {

        //ARRANGE GIVEN     (empty string)
        String token = "";

        //ACT WHEN
        boolean b = jwtUtils.validateJwtToken(token);

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




}
