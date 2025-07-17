package com.microservice.foodcourt.domain.utils;

public class DomainConstants {

    private DomainConstants() {}

    public static final String REGEX_PHONE_NUMBER = "^\\+?[0-9]{1,13}$";
    public static final String REGEX_ONLY_NUMBER = "^[0-9]+$";
    public static final String REGEX_NAME = "^(?!\\d+$).+$";
    public static final int PAGE_MIN = 0;
    public static final int SIZE_MIN = 0;


    public static final String REQUIRED_NAME = "El nombre es requerido.";
    public static final String INVALID_NAME = "El nombre no puede ser formado por solo numeros.";
    public static final String REQUIRED_NIT = "El NIT es requerido.";
    public static final String INVALID_NIT = "El NIT debe ser solo numerico.";
    public static final String REQUIRED_ADDRESS = "La direccion es requerida.";
    public static final String REQUIRED_PHONENUMBER = "El numero de telefono es requerido.";
    public static final String INVALID_PHONENUMBER = "El numero de telefeono tiene un formato invalido, eje: (+573005698325).";
    public static final String REQUIRED_URLLOGO = "El logo es requerido.";
    public static final String REQUIRED_OWNERID = "El Id del propietario es requerido.";

    public static final String REQUIRED_PRICE = "El precio es requerido.";
    public static final String INVALID_PRICE = "El precio debe ser numero entero positivo mayor a cero.";

    public static final String REQUIRED_DESCRIPTION = "La descripcion es requerida.";
    public static final String REQUIRED_URLIMAGE = "La imagen es requerida.";
    public static final String REQUIRED_CATEGORY = "El id de categoria es requerido.";
    public static final String REQUIRED_RESTAURANT = "El id de restaurante es requerido.";

    //Exception constants
    public static final String INVALID_PAGE = "La pagina no puede ser menor a cero.";
    public static final String INVALID_SIZE = "El tamaño de la pagina no puede ser menor a cero.";


    //Update order.
    public static final String ORDER_ALREADY_ASSIGNED_TO_EMPLOYEE = "Ya te asignaste este pedido.";
    public static final String CANNOT_MANAGE_OTHER_RESTAURANT_ORDERS = "No puedes manipular pedidos de otro restaurante.";
    public static final String CHEF_ALREADY_ASSIGNED = "Ya existe un chef asignado a este pedido.";
    public static final String ORDER_HAS_NO_ASSIGNED_CHEF = "Este pedido aun no tiene un chef asignado.";
    public static final String CANNOT_MANAGE_OTHER_CHEF_ORDERS = "No puedes manipular pedidos de otro chef.";
    public static final String ORDER_ALREADY_CANCELLED = "La orden fue cancelada anteriormente.";
    public static final String CANNOT_CANCEL_OTHER_CUSTOMER_ORDER = "No puedes cancelar pedidos de otro cliente.";
    public static final String ORDER_NOT_IN_PREPARATION = "EL pedido no esta en preparacion,no puedes notificar al cliente.";
    public static final String ORDER_NOT_READY_TO_DELIVER = "EL pedido no esta listo,no puedes entregarlo.";
    public static final String ORDER_CANNOT_BE_CANCELLED_AFTER_PREPARATION = "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse.";

}
