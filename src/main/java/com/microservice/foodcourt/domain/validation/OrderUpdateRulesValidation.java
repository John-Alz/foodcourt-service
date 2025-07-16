package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.DataFoundException;
import com.microservice.foodcourt.domain.exception.InvalidOrderStatusException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;
import com.microservice.foodcourt.domain.model.OrderStatusModel;

public class OrderUpdateRulesValidation {


    public void validateAlreadyAssignedToEmployee(boolean isAlreadyAssigned) {
        if (isAlreadyAssigned) throw new DataFoundException("Ya te asignaste este pedido");
    }

    public void validateRestaurantByEmployee(Long restaurantIdByEmployee, Long restaurantId) {
        if (!restaurantIdByEmployee.equals(restaurantId)) {
            throw new UnauthorizedActionException("No puedes manipular pedidos de otro restaurante.");
        }
    }

    public void validateAssociatedChef(Long chefId) {
        if (chefId != null) throw new DataFoundException("Ya existe un chef asignado a este pedido");
    }

    public void validateAssignedChef(Long chefId) {
        if (chefId == null) {
            throw new UnauthorizedActionException("Este pedido aun no tiene un chef asignado.");
        }
    }

    public void validateOrderByChef(Long orderChef, Long chefId) {
        if (!orderChef.equals(chefId)) {
            throw new UnauthorizedActionException("No puedes manipular pedidos de otro chef.");
        }
    }

    public void validateStatusOrder(OrderStatusModel orderStatus, OrderStatusModel statusOrder, String message) {
        if (!orderStatus.equals(statusOrder)) {
            throw new InvalidOrderStatusException(message);
        }
    }

    public void validateOrderCancelled(OrderStatusModel orderStatus) {
        if (orderStatus.equals(OrderStatusModel.CANCELADO)) {
            throw new InvalidOrderStatusException("La orden fue cancelada anteriormente.");
        }
    }

    public void validateOrderByCustomer(Long orderCustomer, Long customerId) {
        if (!orderCustomer.equals(customerId)) {
            throw new UnauthorizedActionException("No puedes cancelar pedidos de otro cliente.");
        }
    }

    public void validateDataUpdate(boolean isAlreadyAssigned, Long chefId, Long restaurantIdByEmployee, Long restaurantId) {
        validateAlreadyAssignedToEmployee(isAlreadyAssigned);
        validateRestaurantByEmployee(restaurantIdByEmployee, restaurantId);
        validateAssociatedChef(chefId);
    }


    public void validateDataMarkReady(Long restaurantIdByEmployee, Long restaurantId, Long orderChef, Long chefId, OrderStatusModel orderStatus) {
        validateRestaurantByEmployee(restaurantIdByEmployee, restaurantId);
        validateAssignedChef(orderChef);
        validateOrderByChef(orderChef, chefId);
        validateStatusOrder(orderStatus, OrderStatusModel.PREPARACION, "EL pedido no esta en preparacion,no puedes notificar al cliente.");
    }

    public void validateDataMarkDelivered(Long restaurantIdByEmployee, Long restaurantId, Long orderChef, Long chefId, OrderStatusModel orderStatus) {
        validateRestaurantByEmployee(restaurantIdByEmployee, restaurantId);
        validateAssignedChef(orderChef);
        validateOrderByChef(orderChef, chefId);
        validateStatusOrder(orderStatus, OrderStatusModel.LISTO, "EL pedido no esta listo,no puedes entregarlo");
    }

    public void validateDataMarkCancelled(Long orderCustomer, Long customerId, OrderStatusModel orderStatus) {
        validateOrderByCustomer(orderCustomer, customerId);
        validateOrderCancelled(orderStatus);
        validateStatusOrder(orderStatus, OrderStatusModel.PENDIENTE, "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse.");
    }


}
