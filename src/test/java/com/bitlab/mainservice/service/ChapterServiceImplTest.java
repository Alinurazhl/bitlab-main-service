package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.ChapterDto;
import com.bitlab.mainservice.entity.Chapter;
import com.bitlab.mainservice.mapper.ChapterMapper;
import com.bitlab.mainservice.repository.ChapterRepository;
import com.bitlab.mainservice.service.impl.ChapterServiceImpl;
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
class ChapterServiceImplTest {
    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private ChapterMapper chapterMapper;
    @InjectMocks
    private ChapterServiceImpl chapterService;
    private Chapter chapter;
    private ChapterDto chapterDto;

    @BeforeEach
    void setUp() {
        chapter = Chapter.builder().id(1L).name("OOP Basics").description("Chapter Description").build();
        chapterDto = ChapterDto.builder().id(1L).name("OOP Basics").description("Chapter Description").courseId(10L).build();
    }

    @Test
    @DisplayName("Должен возвращать главы по courseId")
    void getChaptersByCourseId_ShouldReturnListOfChapterDto() {
        when(chapterRepository.findAllByCourseId(10L)).thenReturn(List.of(chapter));
        when(chapterMapper.toDtoList(anyList())).thenReturn(List.of(chapterDto));
        List<ChapterDto> result = chapterService.getChaptersByCourseId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chapterRepository, times(1)).findAllByCourseId(10L);
    }

    @Test
    @DisplayName("Должен возвращать главу по ID")
    void getChapterById_WhenExists_ShouldReturnChapterDto() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(chapterMapper.toDto(chapter)).thenReturn(chapterDto);

        ChapterDto result = chapterService.getChapterById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(chapterRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку, если глава не найдена")
    void getChapterById_WhenNotFound_ShouldThrowException() {
        when(chapterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> chapterService.getChapterById(99L));
    }
}