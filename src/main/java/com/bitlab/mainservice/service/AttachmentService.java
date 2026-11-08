package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.AttachmentDto;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    AttachmentDto uploadAttachment(MultipartFile file, Long lessonId);
    byte[] downloadAttachment(String url); // Исправлено: добавлен массив байт []
}
