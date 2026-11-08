package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.dto.AttachmentDto;
import com.bitlab.mainservice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    // Точный эндпоинт по ТЗ: /upload
    @PostMapping("/upload")
    public ResponseEntity<AttachmentDto> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lessonId") Long lessonId) {
        return ResponseEntity.ok(attachmentService.uploadAttachment(file, lessonId));
    }

    // Точный эндпоинт по ТЗ: /download
    @GetMapping("/download/{url}")
    public ResponseEntity<byte[]> download(@PathVariable String url) { // Исправлено: byte[]
        byte[] data = attachmentService.downloadAttachment(url);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + url + "\"")
                .body(data);
    }
}
