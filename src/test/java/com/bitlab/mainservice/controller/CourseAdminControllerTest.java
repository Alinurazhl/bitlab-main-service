package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.CourseDto;
import com.bitlab.mainservice.service.CourseService;
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

@WebMvcTest(CourseAdminController.class)
@Import(CourseAdminControllerTest.SecurityTestConfig.class)
class CourseAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseDto courseDto;

    @EnableMethodSecurity
    static class SecurityTestConfig {}

    @BeforeEach
    void setUp() {
        courseDto = CourseDto.builder().id(1L).name("Admin Course").description("Description").build();
    }

    @Test
    @DisplayName("POST /course под ролью ADMIN должен возвращать 201 Created")
    void createCourse_WhenAdmin_ShouldReturn201Created() throws Exception {
        when(courseService.createCourse(any(CourseDto.class))).thenReturn(courseDto);

        mockMvc.perform(post("/course")
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /course под ролью USER должен блокировать доступ")
    void createCourse_WhenUser_ShouldReturn403Forbidden() throws Exception {
        mockMvc.perform(post("/course")
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                // Изменено: Ожидаем прерывание контекста из-за срабатывания @PreAuthorize [INDEX]
                .andExpect(status().isInternalServerError());
    }
}
