package com.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson") // Базовый URL строго по ТЗ для LT17 [1]
@RequiredArgsConstructor
@Tag(name = "Lesson Admin Controller", description = "Управление уроками (Доступ только для Администратора)")
public class LessonAdminController {

    private final LessonService lessonService;

    @PostMapping
    @Operation(summary = "Создать новый урок (Только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')") // Защита уровня метода по ТЗ LT17 [1]
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        LessonDto createdLesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий урок (Только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')") // Защита уровня метода по ТЗ LT17 [1]
    public ResponseEntity<LessonDto> updateLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        return ResponseEntity.ok(lessonService.updateLesson(id, lessonDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить урок по ID (Только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')") // Защита уровня метода по ТЗ LT17 [1]
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}
