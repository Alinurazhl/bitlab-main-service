package com.bitlab.mainservice.repository;

import com.bitlab.mainservice.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChapterRepository extends
        JpaRepository<Chapter, Long> {
    List<Chapter> findAllByCourseId(Long courseId);

}