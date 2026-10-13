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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChapterController.class)
class ChapterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChapterService chapterService;
    @Autowired
    private ObjectMapper objectMapper;
    private ChapterDto chapterDto;

    @BeforeEach
    void setUp() {
        chapterDto = ChapterDto.builder().id(1L).name("OOP Basics").description("Chapter Description").courseId(10L).build();
    }

    @Test
    @DisplayName("GET /api/v1/chapters/course/{courseId} должен возвращать 200 OK")
    void getChaptersByCourseId_ShouldReturn200OK() throws Exception {
        when(chapterService.getChaptersByCourseId(10L)).thenReturn(List.of(chapterDto));
        mockMvc.perform(get("/api/v1/chapters/course/10")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("OOP Basics"));
    }

    @Test
    @DisplayName("GET /api/v1/chapters/{id} должен возвращать 200 OK")
    void getChapterById_ShouldReturn200OK() throws Exception {
        when(chapterService.getChapterById(1L)).thenReturn(chapterDto);
        mockMvc.perform(get("/api/v1/chapters/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("OOP Basics"));
    }

    @Test
    @DisplayName("POST /api/v1/chapters должен создавать главу и возвращать 201 Created")
    void createChapter_ShouldReturn201Created() throws Exception {
        when(chapterService.createChapter(any(ChapterDto.class))).thenReturn(chapterDto);
        mockMvc.perform(post("/api/v1/chapters").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(chapterDto))).andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("OOP Basics"));
    }

    @Test
    @DisplayName("DELETE /api/v1/chapters/{id} должен возвращать 204 No Content")
    void deleteChapter_ShouldReturn204NoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/chapters/1")).andExpect(status().isNoContent());
    }
}