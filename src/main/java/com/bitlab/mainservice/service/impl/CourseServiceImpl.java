package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.CourseDto;
import com.bitlab.mainservice.entity.Course;
import com.bitlab.mainservice.mapper.CourseMapper;
import com.bitlab.mainservice.repository.CourseRepository;
import com.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    @Override
    public List<CourseDto> getAllCourses() {
        log.info("Fetching all courses");
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDtoList(courses);
    }

    @Override
    public CourseDto getCourseById(Long id) {
        log.info("Fetching course by id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course with id {} not found", id);
                    return new IllegalArgumentException("Course not found with id: " + id);
                });
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        log.info("Creating new course");
        log.debug("Creating course object payload: {}", courseDto);

        Course course = courseMapper.toEntity(courseDto);
        Course savedCourse = courseRepository.save(course);

        log.info("Course created successfully with id: {}", savedCourse.getId());
        return courseMapper.toDto(savedCourse);
    }

    @Override
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        log.info("Updating course with id: {}", id);
        log.debug("Update course object payload: {}", courseDto);

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Course with id {} not found", id);
                    return new IllegalArgumentException("Course not found with id: " + id);
                });

        existingCourse.setName(courseDto.getName());
        existingCourse.setDescription(courseDto.getDescription());

        Course updatedCourse = courseRepository.save(existingCourse);
        return courseMapper.toDto(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);
        if (!courseRepository.existsById(id)) {
            log.error("Cannot delete. Course with id {} not found", id);
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}