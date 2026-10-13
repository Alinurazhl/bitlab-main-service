package com.bitlab.mainservice.mapper;

import com.bitlab.mainservice.dto.LessonDto;
import com.bitlab.mainservice.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "chapter.id", target = "chapterId")
    LessonDto toDto(Lesson lesson);

    @Mapping(source = "chapterId", target = "chapter.id")
    Lesson toEntity(LessonDto lessonDto);

    List<LessonDto> toDtoList(List<Lesson> lessons);
}