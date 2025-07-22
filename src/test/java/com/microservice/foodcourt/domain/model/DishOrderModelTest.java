package com.microservice.foodcourt.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DishOrderModelTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        DishOrderModel dishOrder = new DishOrderModel();
        OrderModel order = new OrderModel();
        DishModel dish = new DishModel();
        Integer amount = 3;

        // Act
        dishOrder.setOrder(order);
        dishOrder.setDish(dish);
        dishOrder.setAmount(amount);

        // Assert
        assertEquals(order, dishOrder.getOrder());
        assertEquals(dish, dishOrder.getDish());
        assertEquals(amount, dishOrder.getAmount());
    }

    @Test
    void testDefaultConstructor() {
        DishOrderModel dishOrder = new DishOrderModel();

        assertNull(dishOrder.getOrder());
        assertNull(dishOrder.getDish());
        assertNull(dishOrder.getAmount());
    }
}
