package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.ChapterDto;
import com.bitlab.mainservice.entity.Chapter;
import com.bitlab.mainservice.mapper.ChapterMapper;
import com.bitlab.mainservice.repository.ChapterRepository;
import com.bitlab.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;
    @Override
    public List<ChapterDto> getChaptersByCourseId(Long courseId) {
        log.info("Fetching chapters for course id: {}", courseId);
        List<Chapter> chapters = chapterRepository.findAllByCourseId(courseId);
        return chapterMapper.toDtoList(chapters);
    }

    @Override
    public ChapterDto getChapterById(Long id) {
        log.info("Fetching chapter by id: {}", id);
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Chapter with id {} not found", id);
                    return new IllegalArgumentException("Chapter not found with id: " + id);
                });
        return chapterMapper.toDto(chapter);
    }

    @Override
    public ChapterDto createChapter(ChapterDto chapterDto) {
        log.info("Creating new chapter");
        log.debug("Creating chapter object payload: {}", chapterDto);

        Chapter chapter = chapterMapper.toEntity(chapterDto);
        Chapter savedChapter = chapterRepository.save(chapter);

        log.info("Chapter created successfully with id: {}", savedChapter.getId());
        return chapterMapper.toDto(savedChapter);
    }

    @Override
    public void deleteChapter(Long id) {
        log.info("Deleting chapter with id: {}", id);
        if (!chapterRepository.existsById(id)) {
            log.error("Cannot delete. Chapter with id {} not found", id);
            throw new IllegalArgumentException("Chapter not found with id: " + id);
        }
        chapterRepository.deleteById(id);
    }
}