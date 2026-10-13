package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.entity.Lesson;
import com.bitlab.mainservice.mapper.LessonMapper;
import com.bitlab.mainservice.repository.LessonRepository;
import com.bitlab.mainservice.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private LessonMapper lessonMapper;
    @InjectMocks
    private LessonServiceImpl lessonService;
    private Lesson lesson;
    private LessonDto lessonDto;

    @BeforeEach
    void setUp() {
        lesson = Lesson.builder().id(1L).name("Spring Boot Intro").content("Content text").build();
        lessonDto = LessonDto.builder().id(1L).name("Spring Boot Intro").content("Content text").chapterId(5L).build();
    }

    @Test
    @DisplayName("Должен возвращать уроки по chapterId")
    void getLessonsByChapterId_ShouldReturnListOfLessonDto() {
        when(lessonRepository.findAllByChapterId(5L)).thenReturn(List.of(lesson));
        when(lessonMapper.toDtoList(anyList())).thenReturn(List.of(lessonDto));
        List<LessonDto> result = lessonService.getLessonsByChapterId(5L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(lessonRepository, times(1)).findAllByChapterId(5L);
    }

    @Test
    @DisplayName("Должен возвращать урок по ID")
    void getLessonById_WhenExists_ShouldReturnLessonDto() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDto);

        LessonDto result = lessonService.getLessonById(1L);

        assertNotNull(result);
        assertEquals("Spring Boot Intro", result.getName());
    }
}