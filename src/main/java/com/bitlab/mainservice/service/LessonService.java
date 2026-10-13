package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.LessonDto;

import java.util.List;

public interface LessonService {
    List<LessonDto> getLessonsByChapterId(Long chapterId);
    LessonDto getLessonById(Long id);
    LessonDto createLesson(LessonDto lessonDto);
    void deleteLesson(Long id);
}