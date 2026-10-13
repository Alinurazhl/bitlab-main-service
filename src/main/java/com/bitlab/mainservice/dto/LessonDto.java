package com.bitlab.mainservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO урока")
public class LessonDto {
    @Schema(description = "Уникальный идентификатор урока", example = "1")
    private Long id;
    @Schema(description = "Название урока", example = "Введение в Spring Boot")
    private String name;
    @Schema(description = "Краткое описание урока", example = "Базовые понятия и настройка проекта")
    private String description;
    @Schema(description = "Содержимое/контент урока", example = "Текст или контент урока...")
    private String content;
    @Schema(description = "Порядковый номер урока в главе", example = "1")
    private int order;
    @Schema(description = "Идентификатор главы, к которой относится урок", example = "5")
    private Long chapterId;
    @Schema(description = "Дата и время создания урока")
    private LocalDateTime createdTime;
    @Schema(description = "Дата и время последнего обновления урока")
    private LocalDateTime updatedTime;
}