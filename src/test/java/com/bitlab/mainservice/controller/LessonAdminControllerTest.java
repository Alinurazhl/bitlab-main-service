package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.service.LessonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LessonAdminController.class)
@Import(LessonAdminControllerTest.SecurityTestConfig.class)
class LessonAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    private LessonDto lessonDto;

    @EnableMethodSecurity
    static class SecurityTestConfig {}

    @BeforeEach
    void setUp() {
        lessonDto = LessonDto.builder().id(1L).name("Admin Lesson").content("Content").chapterId(5L).build();
    }

    @Test
    @DisplayName("POST /lesson под ролью ADMIN должен возвращать 201 Created")
    void createLesson_WhenAdmin_ShouldReturn201Created() throws Exception {
        when(lessonService.createLesson(any(LessonDto.class))).thenReturn(lessonDto);

        mockMvc.perform(post("/lesson")
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /lesson под ролью USER должен блокировать доступ")
    void createLesson_WhenUser_ShouldReturnInternalServerError() throws Exception {
        mockMvc.perform(post("/lesson")
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDto)))
                .andExpect(status().isInternalServerError());
    }
}
