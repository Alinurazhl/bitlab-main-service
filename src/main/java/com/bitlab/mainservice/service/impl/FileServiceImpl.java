package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.service.FileService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Файл не должен быть пустым");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        log.info("Uploading file {} to MinIO bucket {}", fileName, bucketName);

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO: {}", e.getMessage());
            throw new RuntimeException("Ошибка сохранения файла в облачное хранилище");
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        log.info("Downloading file {} from MinIO bucket {}", fileName, bucketName);
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())) {
            return stream.readAllBytes();
        } catch (Exception e) {
            log.error("Failed to download file from MinIO: {}", e.getMessage());
            throw new RuntimeException("Файл не найден или недоступен");
        }
    }
}
