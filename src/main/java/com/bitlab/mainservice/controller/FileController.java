package com.bitlab.mainservice.controller;

import com.bitlab.mainservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Tag(name = "File Controller", description = "Загрузка и скачивание медиафайлов")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "Загрузить файл в MinIO")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadFile(file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "Скачать файл из MinIO по имени")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        byte[] data = fileService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(data);
    }
}
