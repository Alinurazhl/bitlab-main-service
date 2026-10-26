package com.bitlab.mainservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import com.bitlab.mainservice.repository.CourseRepository;
import com.bitlab.mainservice.repository.ChapterRepository;
import com.bitlab.mainservice.repository.LessonRepository;

@SpringBootTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none"
})
class MainServiceApplicationTests {

    // Глушим сетевой декодер токенов Keycloak, чтобы Spring не отправлял HTTP-запросы во время сборки
    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private ChapterRepository chapterRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @Test
    void contextLoads() {
    }
}
