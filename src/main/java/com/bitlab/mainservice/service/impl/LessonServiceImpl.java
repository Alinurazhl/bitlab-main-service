package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.entity.Lesson;
import com.bitlab.mainservice.mapper.LessonMapper;
import com.bitlab.mainservice.repository.LessonRepository;
import com.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    @Override
    public List<LessonDto> getLessonsByChapterId(Long chapterId) {
        log.info("Fetching lessons for chapter id: {}", chapterId);
        List<Lesson> lessons = lessonRepository.findAllByChapterId(chapterId);
        return lessonMapper.toDtoList(lessons);
    }

    @Override
    public LessonDto getLessonById(Long id) {
        log.info("Fetching lesson by id: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Lesson with id {} not found", id);
                    return new IllegalArgumentException("Lesson not found with id: " + id);
                });
        return lessonMapper.toDto(lesson);
    }

    @Override
    public LessonDto createLesson(LessonDto lessonDto) {
        log.info("Creating new lesson");
        log.debug("Creating lesson object payload: {}", lessonDto);

        Lesson lesson = lessonMapper.toEntity(lessonDto);
        Lesson savedLesson = lessonRepository.save(lesson);

        log.info("Lesson created successfully with id: {}", savedLesson.getId());
        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public void deleteLesson(Long id) {
        log.info("Deleting lesson with id: {}", id);
        if (!lessonRepository.existsById(id)) {
            log.error("Cannot delete. Lesson with id {} not found", id);
            throw new IllegalArgumentException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }
}