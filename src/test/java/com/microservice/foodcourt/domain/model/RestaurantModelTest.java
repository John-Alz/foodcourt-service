package com.microservice.foodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantModelTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "Burger King";
        String address = "Avenida 123";
        Long ownerId = 10L;
        String phoneNumber = "+573001234567";
        String urlLogo = "https://logo.com/burgerking.png";
        String nit = "900123456";

        // Act
        RestaurantModel restaurant = new RestaurantModel(id, name, address, ownerId, phoneNumber, urlLogo, nit);

        // Assert
        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(ownerId, restaurant.getOwnerId());
        assertEquals(phoneNumber, restaurant.getPhoneNumber());
        assertEquals(urlLogo, restaurant.getUrlLogo());
        assertEquals(nit, restaurant.getNit());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        RestaurantModel restaurant = new RestaurantModel();

        Long id = 2L;
        String name = "Pizza Hut";
        String address = "Calle 45";
        Long ownerId = 20L;
        String phoneNumber = "+573009876543";
        String urlLogo = "https://logo.com/pizzahut.png";
        String nit = "900654321";

        // Act
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setOwnerId(ownerId);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setUrlLogo(urlLogo);
        restaurant.setNit(nit);

        // Assert
        assertEquals(id, restaurant.getId());
        assertEquals(name, restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(ownerId, restaurant.getOwnerId());
        assertEquals(phoneNumber, restaurant.getPhoneNumber());
        assertEquals(urlLogo, restaurant.getUrlLogo());
        assertEquals(nit, restaurant.getNit());
    }
}
