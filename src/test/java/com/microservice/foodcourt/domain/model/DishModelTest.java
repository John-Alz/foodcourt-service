package com.microservice.foodcourt.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DishModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        // Arrange
        DishModel dish = new DishModel();
        CategoryModel category = new CategoryModel(1L, "Bebidas", "Bebidas frías y calientes");
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(100L);

        // Act
        dish.setId(10L);
        dish.setName("Coca Cola");
        dish.setCategory(category);
        dish.setDescription("Refresco");
        dish.setPrice(new BigDecimal("4500.00"));
        dish.setRestaurant(restaurant);
        dish.setImageUrl("https://img.com/coca.jpg");
        dish.setActive(true);

        // Assert
        assertEquals(10L, dish.getId());
        assertEquals("Coca Cola", dish.getName());
        assertEquals(category, dish.getCategory());
        assertEquals("Refresco", dish.getDescription());
        assertEquals(new BigDecimal("4500.00"), dish.getPrice());
        assertEquals(restaurant, dish.getRestaurant());
        assertEquals("https://img.com/coca.jpg", dish.getImageUrl());
        assertTrue(dish.isActive());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        CategoryModel category = new CategoryModel(2L, "Comidas", "Platos fuertes");
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(200L);

        // Act
        DishModel dish = new DishModel(
                20L,
                "Pizza",
                category,
                "Pizza napolitana",
                new BigDecimal("25000"),
                restaurant,
                "https://img.com/pizza.jpg",
                false
        );

        // Assert
        assertEquals(20L, dish.getId());
        assertEquals("Pizza", dish.getName());
        assertEquals(category, dish.getCategory());
        assertEquals("Pizza napolitana", dish.getDescription());
        assertEquals(new BigDecimal("25000"), dish.getPrice());
        assertEquals(restaurant, dish.getRestaurant());
        assertEquals("https://img.com/pizza.jpg", dish.getImageUrl());
        assertFalse(dish.isActive());
    }
}
