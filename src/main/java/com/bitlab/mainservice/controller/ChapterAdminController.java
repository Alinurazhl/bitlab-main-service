package com.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bitlab.mainservice.dto.ChapterDto;
import com.bitlab.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chapter") // Базовый URL строго по ТЗ для LT16
@RequiredArgsConstructor
@Tag(name = "Chapter Admin Controller", description = "Управление главами (Доступ только для Администратора)")
public class ChapterAdminController {

    private final ChapterService chapterService;

    @PostMapping
    @Operation(summary = "Создать новую главу (Только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')") // Защита уровня метода по ТЗ LT16
    public ResponseEntity<ChapterDto> createChapter(@RequestBody ChapterDto chapterDto) {
        ChapterDto createdChapter = chapterService.createChapter(chapterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChapter);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующую главу (Только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')") // Защита уровня метода по ТЗ LT16
    public ResponseEntity<ChapterDto> updateChapter(@PathVariable Long id, @RequestBody ChapterDto chapterDto) {
        return ResponseEntity.ok(chapterService.updateChapter(id, chapterDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить главу по ID (Только для ADMIN)")
    @PreAuthorize("hasRole('ADMIN')") // Защита уровня метода по ТЗ LT16
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }
}
