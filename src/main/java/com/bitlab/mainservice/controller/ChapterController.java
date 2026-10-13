package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.ChapterDto;
import com.bitlab.mainservice.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;import java.util.List;

@RestController
@RequestMapping("/api/v1/chapters")
@RequiredArgsConstructor
@Tag(name = "Chapter Controller", description = "Управление главами внутри курсов")
public class ChapterController {
    private final ChapterService chapterService;
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Получить все главы конкретного курса")
    @ApiResponse(responseCode = "200", description = "Список глав успешно получен")
    public ResponseEntity<List<ChapterDto>> getChaptersByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(chapterService.getChaptersByCourseId(courseId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить главу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Глава найдена"),
            @ApiResponse(responseCode = "404", description = "Глава с указанным ID не найдена")
    })
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.getChapterById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новую главу")
    @ApiResponse(responseCode = "201", description = "Глава успешно создана")
    public ResponseEntity<ChapterDto> createChapter(@RequestBody ChapterDto chapterDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.createChapter(chapterDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить главу по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Глава успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}