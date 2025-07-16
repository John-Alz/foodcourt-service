package com.microservice.foodcourt.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderModelTest {

    @Test
    void testDefaultConstructorAndSetters() {
        OrderModel order = new OrderModel();

        LocalDateTime now = LocalDateTime.now();
        RestaurantModel restaurant = new RestaurantModel();
        DishOrderModel dishOrder = new DishOrderModel();
        List<DishOrderModel> dishes = List.of(dishOrder);

        order.setId(1L);
        order.setCustomerId(101L);
        order.setDate(now);
        order.setStatus(OrderStatusModel.ENTREGADO);
        order.setChefId(55L);
        order.setRestaurant(restaurant);
        order.setDishes(dishes);

        assertEquals(1L, order.getId());
        assertEquals(101L, order.getCustomerId());
        assertEquals(now, order.getDate());
        assertEquals(OrderStatusModel.ENTREGADO, order.getStatus());
        assertEquals(55L, order.getChefId());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(dishes, order.getDishes());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime date = LocalDateTime.of(2025, 7, 10, 12, 0);
        RestaurantModel restaurant = new RestaurantModel();
        DishOrderModel dishOrder = new DishOrderModel();
        List<DishOrderModel> dishes = List.of(dishOrder);

        OrderModel order = new OrderModel(
                2L,
                202L,
                date,
                OrderStatusModel.PREPARACION,
                99L,
                restaurant,
                dishes,
                "123456"
        );

        assertEquals(2L, order.getId());
        assertEquals(202L, order.getCustomerId());
        assertEquals(date, order.getDate());
        assertEquals(OrderStatusModel.PREPARACION, order.getStatus());
        assertEquals(99L, order.getChefId());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(dishes, order.getDishes());
    }

    @Test
    void testDefaultValuesInConstructor() {
        OrderModel order = new OrderModel();

        assertNotNull(order.getDate(), "Date should be initialized");
        assertEquals(OrderStatusModel.PENDIENTE, order.getStatus(), "Default status should be PENDIENTE");
    }
}
