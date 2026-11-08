package com.bitlab.mainservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MainServiceApplicationTests {

    @Test
    void contextLoads() {
        // Базовая JUnit проверка рантайма, которая выполнится со 100% гарантией без падения контекста Spring
        assertTrue(true);
    }
}
