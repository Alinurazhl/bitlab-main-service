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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseDto courseDto;

    @BeforeEach
    void setUp() {
        courseDto = CourseDto.builder().id(1L).name("Java Developer").description("Course description").build();
    }

    @Test
    @DisplayName("GET /api/v1/courses должен возвращать 200 OK")
    void getAllCourses_ShouldReturn200OK() throws Exception {
        when(courseService.getAllCourses()).thenReturn(List.of(courseDto));

        mockMvc.perform(get("/api/v1/courses").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java Developer"));
    }

    @Test
    @DisplayName("GET /api/v1/courses/{id} должен возвращать 200 OK")
    void getCourseById_ShouldReturn200OK() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(courseDto);

        mockMvc.perform(get("/api/v1/courses/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Developer"));
    }

    @Test
    @DisplayName("POST /api/v1/courses должен создавать курс и возвращать 201 Created")
    void createCourse_ShouldReturn201Created() throws Exception {
        when(courseService.createCourse(any(CourseDto.class))).thenReturn(courseDto);

        mockMvc.perform(post("/api/v1/courses").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java Developer"));
    }

    @Test
    @DisplayName("DELETE /api/v1/courses/{id} должен возвращать 204 No Content")
    void deleteCourse_ShouldReturn204NoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/courses/1").with(jwt()))
                .andExpect(status().isNoContent());
    }
}
