package com.microservice.foodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        // Arrange
        CategoryModel category = new CategoryModel();

        // Act
        category.setId(1L);
        category.setName("Bebidas");
        category.setDescription("Categoría de bebidas");

        // Assert
        assertEquals(1L, category.getId());
        assertEquals("Bebidas", category.getName());
        assertEquals("Categoría de bebidas", category.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange & Act
        CategoryModel category = new CategoryModel(2L, "Comidas", "Categoría de comidas");

        // Assert
        assertEquals(2L, category.getId());
        assertEquals("Comidas", category.getName());
        assertEquals("Categoría de comidas", category.getDescription());
    }
}
