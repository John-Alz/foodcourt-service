package com.microservice.foodcourt.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class OrderModel {

    private Long id;
    private Long customerId;
    private LocalDateTime date = LocalDateTime.now();
    private OrderStatusModel status = OrderStatusModel.PENDIENTE;
    private Long chefId;
    private RestaurantModel restaurant;
    private List<DishOrderModel> dishes;

    public OrderModel() {}

    public OrderModel(Long id, Long customerId, LocalDateTime date, OrderStatusModel status, Long chefId, RestaurantModel restaurant, List<DishOrderModel> dishes) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.status = status;
        this.chefId = chefId;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatusModel getStatus() {
        return status;
    }

    public void setStatus(OrderStatusModel status) {
        this.status = status;
    }

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public RestaurantModel getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantModel restaurant) {
        this.restaurant = restaurant;
    }

    public List<DishOrderModel> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishOrderModel> dishes) {
        this.dishes = dishes;
    }
}
