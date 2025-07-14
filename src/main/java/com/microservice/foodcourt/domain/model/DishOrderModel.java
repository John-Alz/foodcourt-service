package com.microservice.foodcourt.domain.model;

public class DishOrderModel {

    private OrderModel order;
    private DishModel dish;
    private Integer amount;

    public DishOrderModel(){}

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public DishModel getDish() {
        return dish;
    }

    public void setDish(DishModel dish) {
        this.dish = dish;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
