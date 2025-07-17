package com.microservice.foodcourt.application.utils;

public class ApplicationConstants {

    private ApplicationConstants(){}

    //Dish
    public static final String CREATED_DISH_MESSAGE = "Plato creado.";
    public static final String UPDATED_DISH_MESSAGE = "Plato actualizado.";
    public static final String UPDATED_DISH_STATUS_MESSAGE = "El estado del plato fue modificado.";

    //Order
    public static final String CREATED_ORDER_MESSAGE = "Pedido creado.";
    public static final String STATUS_PREPARATION_ORDER_MESSAGE = "El pedido esta en preparacion";
    public static final String STATUS_READY_ORDER_MESSAGE = "El pedido esta listo.";
    public static final String STATUS_DELIVERED_ORDER_MESSAGE = "El pedido entregado.";
    public static final String STATUS_CANCELLED_ORDER_MESSAGE = "Pedido cancelado.";

    //Restaurant
    public static final String CREATED_RESTAURANT_MESSAGE = "Restaurante creado.";

}
