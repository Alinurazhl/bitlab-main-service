package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.AttachmentDto;
import com.bitlab.mainservice.entity.Attachment;
import com.bitlab.mainservice.entity.Lesson;
import com.bitlab.mainservice.repository.AttachmentRepository;
import com.bitlab.mainservice.repository.LessonRepository;
import com.bitlab.mainservice.service.AttachmentService;
import com.bitlab.mainservice.service.FileService; // Подключаем твой S3 сервис из LT18
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final LessonRepository lessonRepository;
    private final FileService fileService; // Внедряем бин для работы с MinIO

    @Override
    @Transactional
    public AttachmentDto uploadAttachment(MultipartFile file, Long lessonId) {
        log.info("Checking upload permissions for lesson id: {}", lessonId);

        // 1. Проверка ролей из ТЗ: только ADMIN и TEACHER могут добавлять
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isAuthorized = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_TEACHER"));

        if (!isAuthorized) {
            throw new AccessDeniedException("Доступ запрещен. Только Администратор или Преподаватель может добавлять вложения.");
        }

        // 2. Проверка урока на существование (из твоих заметок, чтобы исключить висящие данные)
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Урок с id " + lessonId + " не найден!"));

        // 3. Загружаем файл физически в MinIO S3 через готовый FileService
        String uniqueUrl = fileService.uploadFile(file);

        // 4. Регистрируем запись о вложении в базе данных PostgreSQL
        Attachment attachment = Attachment.builder()
                .name(file.getOriginalFilename())
                .url(uniqueUrl)
                .lesson(lesson)
                .createdTime(LocalDateTime.now())
                .build();

        Attachment savedAttachment = attachmentRepository.save(attachment);

        return AttachmentDto.builder()
                .id(savedAttachment.getId())
                .name(savedAttachment.getName())
                .url(savedAttachment.getUrl())
                .lessonId(lesson.getId())
                .createdTime(savedAttachment.getCreatedTime())
                .build();
    }

    @Override
    public byte[] downloadAttachment(String url) {
        log.info("Processing attachment download request for url: {}", url);

        // 1. Ищем информацию о файле в БД
        Attachment attachment = attachmentRepository.findByUrl(url)
                .orElseThrow(() -> new IllegalArgumentException("Вложение с указанным URL не зарегистрировано в системе"));

        // 2. Секьюрити-чек ментора: ADMIN и TEACHER скачивают без ограничений
        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isAdminOrTeacher = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_TEACHER"));

        if (!isAdminOrTeacher) {
            // 3. Из твоих заметок: вытаскиваем по цепочке ID курса и проверяем доступ студента
            Long courseId = attachment.getLesson().getChapter().getCourse().getId();
            log.info("Checking student purchase/access level for course id: {}", courseId);

            boolean hasCourseAccess = true; // Заглушка: Студент имеет доступ к курсу

            if (!hasCourseAccess) {
                throw new AccessDeniedException("Скачивание заблокировано. У вас нет доступа к этому курсу.");
            }
        }

        // 4. Твоя строчка: стягиваем оригинальный поток байт из MinIO и отдаем пользователю
        return fileService.downloadFile(url);
    }
}
