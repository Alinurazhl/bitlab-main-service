package com.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bitlab.mainservice.dto.CourseDto;
import com.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Course Controller", description = "Управление курсами LMS системы")
public class CourseController {
    private final CourseService courseService;
    @GetMapping
    @Operation(summary = "Получить список всех курсов", description = "Возвращает массив всех доступных курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить курс по ID", description = "Возвращает детальную информацию о курсе по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс с таким ID не найден")
    })
    public ResponseEntity<CourseDto> getCourseById(
            @Parameter(description = "Идентификатор курса", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый курс", description = "Принимает данные курса и сохраняет его в базе")
    @ApiResponse(responseCode = "201", description = "Курс успешно создан")
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий курс", description = "Обновляет название и описание курса по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Курс для обновления не найден")
    })
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseDto courseDto) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить курс по ID", description = "Удаляет курс из системы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Курс успешно удален"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}