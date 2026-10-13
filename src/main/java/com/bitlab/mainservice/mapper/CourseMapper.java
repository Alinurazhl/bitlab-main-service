package com.bitlab.mainservice.mapper;

import com.bitlab.mainservice.dto.CourseDto;
import com.bitlab.mainservice.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDto toDto(Course course);

    Course toEntity(CourseDto courseDto);

    List<CourseDto> toDtoList(List<Course> courses);
}