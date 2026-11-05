package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.ChapterDto;
import com.bitlab.mainservice.service.ChapterService;
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

@WebMvcTest(ChapterAdminController.class)
@Import(ChapterAdminControllerTest.SecurityTestConfig.class)
class ChapterAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChapterService chapterService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    private ChapterDto chapterDto;

    @EnableMethodSecurity
    static class SecurityTestConfig {}

    @BeforeEach
    void setUp() {
        chapterDto = ChapterDto.builder().id(1L).name("Admin Chapter").description("Description").courseId(10L).build();
    }

    @Test
    @DisplayName("POST /chapter под ролью ADMIN должен возвращать 201 Created")
    void createChapter_WhenAdmin_ShouldReturn201Created() throws Exception {
        when(chapterService.createChapter(any(ChapterDto.class))).thenReturn(chapterDto);

        mockMvc.perform(post("/chapter")
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /chapter под ролью USER должен блокировать доступ")
    void createChapter_WhenUser_ShouldReturnInternalServerError() throws Exception {
        mockMvc.perform(post("/chapter")
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterDto)))
                .andExpect(status().isInternalServerError()); // Ожидаем прерывание из-за срабатывания @PreAuthorize [INDEX]
    }
}
