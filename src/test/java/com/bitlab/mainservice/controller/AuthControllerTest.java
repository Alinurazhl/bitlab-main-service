package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.JwtResponse;
import com.bitlab.mainservice.dto.LoginRequest;
import com.bitlab.mainservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Полностью отключаем фильтры безопасности для публичного эндпоинта логина в тесте
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("admin", "admin");
        jwtResponse = new JwtResponse("access_secret", "refresh_secret", 300L, 604800L, "Bearer");
    }

    @Test
    @DisplayName("POST /api/v1/auth/login должен возвращать токены и статус 200 OK")
    void login_ShouldReturnTokensAnd200OK() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_secret"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
    @Test
    @DisplayName("POST /api/v1/auth/refresh должен обновлять JWT и возвращать 200 OK")
    void refreshToken_ShouldReturnNewTokensAnd200OK() throws Exception {
        com.bitlab.mainservice.dto.RefreshTokenRequest refreshRequest = new com.bitlab.mainservice.dto.RefreshTokenRequest("old_refresh_secret");
        when(authService.refreshToken(any(com.bitlab.mainservice.dto.RefreshTokenRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access_secret"))
                .andExpect(jsonPath("$.refreshToken").value("refresh_secret"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register под ролью ADMIN должен возвращать 201 Created")
    void registerUser_WhenAdmin_ShouldReturn201Created() throws Exception {
        com.bitlab.mainservice.dto.UserRegistrationDto regDto =
                new com.bitlab.mainservice.dto.UserRegistrationDto("teacher1", "t@b.com", "pass", "Ivan", "Ivanov", "TEACHER");

        doNothing().when(authService).registerUser(any(com.bitlab.mainservice.dto.UserRegistrationDto.class));

        mockMvc.perform(post("/api/v1/auth/register")
                        .with(jwt().authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regDto)))
                .andExpect(status().isCreated());
    }

}
