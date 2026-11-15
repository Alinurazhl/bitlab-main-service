package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.AttachmentDto;
import com.bitlab.mainservice.entity.Attachment;
import com.bitlab.mainservice.entity.Lesson;
import com.bitlab.mainservice.repository.AttachmentRepository;
import com.bitlab.mainservice.repository.LessonRepository;
import com.bitlab.mainservice.service.AttachmentService;
import com.bitlab.mainservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
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
    private final FileService fileService;
    // TODO: Добавь сюда зависимость своего репозитория доступа к курсам:
    // private final UserCourseRepository userCourseRepository;

    @Override
    @Transactional
    public AttachmentDto uploadAttachment(MultipartFile file, Long lessonId) {
        log.info("Checking upload permissions for lesson id: {}", lessonId);

        var authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean isAuthorized = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_TEACHER"));

        if (!isAuthorized) {
            throw new AccessDeniedException("Доступ запрещен. Только Администратор или Преподаватель может добавлять вложения.");
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Урок с id " + lessonId + " не найден!"));

        String uniqueUrl = fileService.uploadFile(file);

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
    public InputStreamResource downloadAttachment(String url) {
        log.info("Processing stream download request for url: {}", url);

        Attachment attachment = attachmentRepository.findByUrl(url)
                .orElseThrow(() -> new IllegalArgumentException("Вложение с указанным URL не зарегистрировано в системе"));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdminOrTeacher = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_TEACHER"));

        if (!isAdminOrTeacher) {
            String userId = authentication.getName();
            Long courseId = attachment.getLesson().getChapter().getCourse().getId();

            log.info("Checking course access in DB for user: {} and course: {}", userId, courseId);

            boolean hasCourseAccess = checkStudentAccessInDb(userId, courseId);

            if (!hasCourseAccess) {
                throw new AccessDeniedException("Скачивание заблокировано. Вы не записаны на этот курс.");
            }
        }

        return fileService.downloadFileStream(url);
    }

    private boolean checkStudentAccessInDb(String userId, Long courseId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        String requiredAuthority = "ROLE_COURSE_" + courseId;

        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(requiredAuthority));
    }

}