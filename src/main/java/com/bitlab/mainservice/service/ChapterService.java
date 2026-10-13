package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.ChapterDto;
import java.util.List;

public interface ChapterService {

    List<ChapterDto> getChaptersByCourseId(Long courseId);

    ChapterDto getChapterById(Long id);

    ChapterDto createChapter(ChapterDto chapterDto);

    void deleteChapter(Long id);
}
