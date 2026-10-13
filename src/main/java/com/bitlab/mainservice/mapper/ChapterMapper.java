package com.bitlab.mainservice.mapper;

import com.bitlab.mainservice.dto.ChapterDto;
import com.bitlab.mainservice.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    @Mapping(source = "course.id", target = "courseId")
    ChapterDto toDto(Chapter chapter);

    @Mapping(source = "courseId", target = "course.id")
    Chapter toEntity(ChapterDto chapterDto);

    List<ChapterDto> toDtoList(List<Chapter> chapters);
}