package com.bitlab.mainservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO курса")
public class CourseDto {
    @Schema(description = "Уникальный идентификатор курса", example = "1")
    private Long id;
    @Schema(description = "Название курса", example = "Java Developer")
    private String name;
    @Schema(description = "Описание курса", example = "Полный курс по разработке на Java Spring Boot")
    private String description;
    @Schema(description = "Дата и время создания курса")
    private LocalDateTime createdTime;
    @Schema(description = "Дата и время последнего обновления курса")
    private LocalDateTime updatedTime;
}