package com.openclassrooms.starterjwt.security;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;



//Tests de la sécurité de l'application :
@SpringBootTest
@AutoConfigureMockMvc
public class AuthTokenFilterIntegrationTest {

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;

    @Test
    void loginShouldNotAccessPublicEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isMethodNotAllowed());//post expected
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isBadRequest());//get ok but no content
    }

    @Test
    void registerShouldNotAccessPublicEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/auth/register")) 
                .andExpect(status().isMethodNotAllowed());//post expected
        mockMvc.perform(post("/api/auth/register"))
                .andExpect(status().isBadRequest()); //get ok but no content
    }

    @Test
    void shouldRejectProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void shouldAuthenticateWithValidToken_BEWARE_ACCESS_TO_PROD_DB_WITH_DATA_OF_PROD_DB__INTEGRATION_TEST() throws Exception {
        String token = "valid-jwt";

        when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(Constantes.STRING_EMAIL_YOGA);

        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"email\":\"yoga@studio.com\",\"lastName\":\"ADMIN\",\"firstName\":\"admin\",\"admin\":true,\"createdAt\":\"2025-12-21T22:41:25\",\"updatedAt\":\"2026-04-22T23:22:40\"}"));
    }


    @Test
    void shouldRejectInvalidToken() throws Exception {
        String token = "invalid-jwt";

        when(jwtUtils.validateJwtToken(token)).thenReturn(false); //FALSE
        when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(Constantes.STRING_EMAIL_YOGA);
        
        mockMvc.perform(get("/api/user/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized()); // ou 401
    }


}
