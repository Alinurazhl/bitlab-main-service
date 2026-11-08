package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.entity.Lesson;
import com.bitlab.mainservice.mapper.LessonMapper;
import com.bitlab.mainservice.repository.LessonRepository;
import com.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // Оптимизация транзакций чтения для всего класса
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final com.bitlab.mainservice.repository.ChapterRepository chapterRepository;

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
    @Transactional
    public LessonDto createLesson(LessonDto lessonDto) {
        log.info("Creating new lesson");

        // Валидация ментора: проверяем, что глава физически существует в БД
        if (!chapterRepository.existsById(lessonDto.getChapterId())) {
            throw new IllegalArgumentException("Невозможно создать урок: Глава с ID " + lessonDto.getChapterId() + " не существует!");
        }

        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setId(null);
        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(savedLesson);
    }


    @Override
    @Transactional // Добавленный метод обновления по ТЗ LT17
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        log.info("Updating lesson with id: {}", id);
        log.debug("Updating lesson object payload: {}", lessonDto);

        Lesson existingLesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Lesson with id {} not found", id);
                    return new IllegalArgumentException("Lesson not found with id: " + id);
                });

        existingLesson.setName(lessonDto.getName());
        existingLesson.setContent(lessonDto.getContent());

        Lesson updatedLesson = lessonRepository.save(existingLesson);
        log.info("Lesson updated successfully with id: {}", updatedLesson.getId());
        return lessonMapper.toDto(updatedLesson);
    }

    @Override
    @Transactional // Изолированная транзакция удаления
    public void deleteLesson(Long id) {
        log.info("Deleting lesson with id: {}", id);
        if (!lessonRepository.existsById(id)) {
            log.error("Cannot delete. Lesson with id {} not found", id);
            throw new IllegalArgumentException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }
}
