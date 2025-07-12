package com.microservice.foodcourt.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        PageResult<String> pageResult = new PageResult<>();

        pageResult.setContent(List.of("A", "B", "C"));
        pageResult.setPage(1);
        pageResult.setSize(3);
        pageResult.setTotalPages(2);
        pageResult.setTotalElements(6L);

        assertEquals(List.of("A", "B", "C"), pageResult.getContent());
        assertEquals(1, pageResult.getPage());
        assertEquals(3, pageResult.getSize());
        assertEquals(2, pageResult.getTotalPages());
        assertEquals(6L, pageResult.getTotalElements());
    }

    @Test
    void testAllArgsConstructor() {
        List<String> content = List.of("X", "Y");
        PageResult<String> pageResult = new PageResult<>(content, 0, 2, 5, 10L);

        assertEquals(content, pageResult.getContent());
        assertEquals(0, pageResult.getPage());
        assertEquals(2, pageResult.getSize());
        assertEquals(5, pageResult.getTotalPages());
        assertEquals(10L, pageResult.getTotalElements());
    }
}
