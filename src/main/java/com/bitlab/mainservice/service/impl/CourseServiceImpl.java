package com.bitlab.mainservice.service.impl;

import com.bitlab.mainservice.dto.CourseDto;
import com.bitlab.mainservice.entity.Course;
import com.bitlab.mainservice.mapper.CourseMapper;
import com.bitlab.mainservice.repository.CourseRepository;
import com.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 2. Оптимизация транзакций на чтение для всего класса
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseDto> getAllCourses() {
        log.info("Fetching all courses");

        // 3. Исправлено: Ограничение выборки до 100 элементов (пагинация)
        Pageable pageable = PageRequest.of(0, 100);
        Page<Course> coursePage = courseRepository.findAll(pageable);

        return courseMapper.toDtoList(coursePage.getContent());
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
    @Transactional // 2. Исправлено: Атомарность записи
    public CourseDto createCourse(CourseDto courseDto) {
        log.info("Creating new course");
        log.debug("Creating course object payload: {}", courseDto);

        Course course = courseMapper.toEntity(courseDto);

        // 1. Исправлено: Игнорируем ID от клиента
        course.setId(null);

        Course savedCourse = courseRepository.save(course);

        log.info("Course created successfully with id: {}", savedCourse.getId());
        return courseMapper.toDto(savedCourse);
    }

    @Override
    @Transactional // 2. Исправлено: Атомарность обновления
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
    @Transactional // 2 и 4. Исправлено: Атомарное безопасное удаление
    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);
        if (!courseRepository.existsById(id)) {
            log.error("Cannot delete. Course with id {} not found", id);
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
