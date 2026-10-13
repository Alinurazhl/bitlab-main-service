package com.bitlab.mainservice.service;

import com.bitlab.mainservice.dto.CourseDto;
import com.bitlab.mainservice.entity.Course;
import com.bitlab.mainservice.mapper.CourseMapper;
import com.bitlab.mainservice.repository.CourseRepository;
import com.bitlab.mainservice.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseMapper courseMapper;
    @InjectMocks
    private CourseServiceImpl courseService;
    private Course course;
    private CourseDto courseDto;

    @BeforeEach
    void setUp() {
        course = Course.builder().id(1L).name("Java Developer").description("Course description").build();
        courseDto = CourseDto.builder().id(1L).name("Java Developer").description("Course description").build();
    }

    @Test
    @DisplayName("Должен возвращать список всех курсов")
    void getAllCourses_ShouldReturnListOfCourseDto() {
        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(courseMapper.toDtoList(anyList())).thenReturn(List.of(courseDto));
        List<CourseDto> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Должен возвращать курс по ID")
    void getCourseById_WhenCourseExists_ShouldReturnCourseDto() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        CourseDto result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Должен выбрасывать IllegalArgumentException, если курс не найден")
    void getCourseById_WhenCourseDoesNotExist_ShouldThrowException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.getCourseById(99L));
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Должен успешно создавать курс")
    void createCourse_ShouldSaveAndReturnCourseDto() {
        when(courseMapper.toEntity(courseDto)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        CourseDto result = courseService.createCourse(courseDto);

        assertNotNull(result);
        assertEquals("Java Developer", result.getName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    @DisplayName("Должен успешно удалять курс по ID")
    void deleteCourse_WhenCourseExists_ShouldDelete() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));
        verify(courseRepository, times(1)).deleteById(1L);
    }
}