package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "Lesson Controller", description = "Управление уроками")
public class LessonController {
    private final LessonService lessonService;
    @GetMapping("/chapter/{chapterId}")
    @Operation(summary = "Получить все уроки конкретной главы")
    @ApiResponse(responseCode = "200", description = "Список уроков успешно получен")
    public ResponseEntity<List<LessonDto>> getLessonsByChapterId(@PathVariable Long chapterId) {
        return ResponseEntity.ok(lessonService.getLessonsByChapterId(chapterId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить урок по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Урок найден"),
            @ApiResponse(responseCode = "404", description = "Урок с указанным ID не найден")
    })
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый урок")
    @ApiResponse(responseCode = "201", description = "Урок успешно создан")
    public ResponseEntity<LessonDto> createLesson(@RequestBody LessonDto lessonDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLesson(lessonDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить урок по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Урок успешно удален"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}