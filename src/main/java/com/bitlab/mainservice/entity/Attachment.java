package com.bitlab.mainservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name; // VARCHAR 255 по ТЗ

    @Column(name = "url", nullable = false)
    private String url; // VARCHAR 255 по ТЗ, по нему стягиваем файл

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson; // К какому уроку прикреплено вложение по ТЗ

    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;
}
