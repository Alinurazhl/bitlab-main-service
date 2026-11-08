package com.bitlab.mainservice.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentDto {
    private Long id;
    private String name;
    private String url;
    private Long lessonId;
    private LocalDateTime createdTime;
}
