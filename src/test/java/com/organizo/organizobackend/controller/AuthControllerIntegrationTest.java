package com.organizo.organizobackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organizo.organizobackend.dto.LoginRequest;
import com.organizo.organizobackend.dto.RegistroRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    @Test
    void registerELogin_deveRetornarToken() throws Exception {
        // Monta payload de registro
        RegistroRequest reg = new RegistroRequest();
        reg.setEmail("test@ex.com");
        reg.setSenha("123456");
        reg.setNome("Teste");
        reg.setRole("CLIENTE");

        // Chama /register
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(reg)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());

        // Monta payload de login
        LoginRequest login = new LoginRequest();
        login.setEmail("test@ex.com");
        login.setSenha("123456");

        // Chama /login
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}
