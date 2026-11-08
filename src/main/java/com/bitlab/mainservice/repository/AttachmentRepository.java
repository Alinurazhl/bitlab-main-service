package com.bitlab.mainservice.repository;

import com.bitlab.mainservice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByUrl(String url); // Поиск по url для стягивания файла по ТЗ
}
