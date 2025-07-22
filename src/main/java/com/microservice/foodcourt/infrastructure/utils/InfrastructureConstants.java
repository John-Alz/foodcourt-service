package com.microservice.foodcourt.infrastructure.utils;

public class InfrastructureConstants {
    private InfrastructureConstants() {}

    //PreAuthorize
    public static final String HAS_ROLE_OWNER = "hasRole('OWNER')";
    public static final String HAS_ROLE_CUSTOMER= "hasRole('CUSTOMER')";
    public static final String HAS_ROLE_EMPLOYEE= "hasRole('EMPLOYEE')";
    public static final String HAS_ROLE_ADMIN= "hasRole('ADMINISTRATOR')";

    //Category

    public static final String CATEGORY_NOT_FOUND = "Categoria no econtrada.";

    //Dish
    public static final String DISH_NOT_FOUND = "No existe el plato con ese id.";
    public static final String DISHES_NOT_FROM_RESTAURANT = "Uno o más platos no pertenecen al restaurante.";

    //Order
    public static final String ORDER_NOT_FOUND = "No existe una orden con ese id.";

    //Restaurant
    public static final String RESTAURANT_NOT_FOUND = "Restaurante no encontrado.";
    public static final String RESTAURANT_NOT_FOUND_FOR_USER = "No se encontró ningún restaurante al que pertenece el usuario.";


}
