package com.bitlab.mainservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO главы")
public class ChapterDto {
    @Schema(description = "Уникальный идентификатор главы", example = "1")
    private Long id;
    @Schema(description = "Название главы", example = "Основы ООП в Java")
    private String name;
    @Schema(description = "Описание главы", example = "Классы, объекты, наследование и полиморфизм")
    private String description;
    @Schema(description = "Порядковый номер главы в курсе", example = "1")
    private int order;
    @Schema(description = "Идентификатор курса, к которому относится глава", example = "10")
    private Long courseId;
    @Schema(description = "Дата и время создания главы")
    private LocalDateTime createdTime;
    @Schema(description = "Дата и время последнего обновления главы")
    private LocalDateTime updatedTime;
}