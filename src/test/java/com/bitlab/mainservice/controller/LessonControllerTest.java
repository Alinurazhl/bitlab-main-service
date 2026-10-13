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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    private LessonDto lessonDto;

    @BeforeEach
    void setUp() {
        lessonDto = LessonDto.builder()
                .id(1L)
                .name("Spring Boot Intro")
                .content("Content text")
                .chapterId(5L)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/lessons/chapter/{chapterId} должен возвращать 200 OK")
    void getLessonsByChapterId_ShouldReturn200OK() throws Exception {
        when(lessonService.getLessonsByChapterId(5L)).thenReturn(List.of(lessonDto));

        mockMvc.perform(get("/api/v1/lessons/chapter/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Spring Boot Intro"));
    }

    @Test
    @DisplayName("GET /api/v1/lessons/{id} должен возвращать 200 OK")
    void getLessonById_ShouldReturn200OK() throws Exception {
        when(lessonService.getLessonById(1L)).thenReturn(lessonDto);

        mockMvc.perform(get("/api/v1/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Spring Boot Intro"));
    }

    @Test
    @DisplayName("POST /api/v1/lessons должен создавать урок и возвращать 201 Created")
    void createLesson_ShouldReturn201Created() throws Exception {
        when(lessonService.createLesson(any(LessonDto.class))).thenReturn(lessonDto);

        mockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Spring Boot Intro"));
    }

    @Test
    @DisplayName("DELETE /api/v1/lessons/{id} должен возвращать 204 No Content")
    void deleteLesson_ShouldReturn204NoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/lessons/1"))
                .andExpect(status().isNoContent());
    }
}
