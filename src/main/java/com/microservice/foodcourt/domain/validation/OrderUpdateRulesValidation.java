package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.DataFoundException;
import com.microservice.foodcourt.domain.exception.InvalidOrderStatusException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.utils.DomainConstants;

public class OrderUpdateRulesValidation {


    public void validateAlreadyAssignedToEmployee(boolean isAlreadyAssigned) {
        if (isAlreadyAssigned) throw new DataFoundException(DomainConstants.ORDER_ALREADY_ASSIGNED_TO_EMPLOYEE);
    }

    public void validateRestaurantByEmployee(Long restaurantIdByEmployee, Long restaurantId) {
        if (!restaurantIdByEmployee.equals(restaurantId)) {
            throw new UnauthorizedActionException(DomainConstants.CANNOT_MANAGE_OTHER_RESTAURANT_ORDERS);
        }
    }

    public void validateAssociatedChef(Long chefId) {
        if (chefId != null) throw new DataFoundException(DomainConstants.CHEF_ALREADY_ASSIGNED);
    }

    public void validateAssignedChef(Long chefId) {
        if (chefId == null) {
            throw new UnauthorizedActionException(DomainConstants.ORDER_HAS_NO_ASSIGNED_CHEF);
        }
    }

    public void validateOrderByChef(Long orderChef, Long chefId) {
        if (!orderChef.equals(chefId)) {
            throw new UnauthorizedActionException(DomainConstants.CANNOT_MANAGE_OTHER_CHEF_ORDERS);
        }
    }

    public void validateStatusOrder(OrderStatusModel orderStatus, OrderStatusModel statusOrder, String message) {
        if (!orderStatus.equals(statusOrder)) {
            throw new InvalidOrderStatusException(message);
        }
    }

    public void validateOrderCancelled(OrderStatusModel orderStatus) {
        if (orderStatus.equals(OrderStatusModel.CANCELADO)) {
            throw new InvalidOrderStatusException(DomainConstants.ORDER_ALREADY_CANCELLED);
        }
    }

    public void validateOrderByCustomer(Long orderCustomer, Long customerId) {
        if (!orderCustomer.equals(customerId)) {
            throw new UnauthorizedActionException(DomainConstants.CANNOT_CANCEL_OTHER_CUSTOMER_ORDER);
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
        validateStatusOrder(orderStatus, OrderStatusModel.PREPARACION, DomainConstants.ORDER_NOT_IN_PREPARATION);
    }

    public void validateDataMarkDelivered(Long restaurantIdByEmployee, Long restaurantId, Long orderChef, Long chefId, OrderStatusModel orderStatus) {
        validateRestaurantByEmployee(restaurantIdByEmployee, restaurantId);
        validateAssignedChef(orderChef);
        validateOrderByChef(orderChef, chefId);
        validateStatusOrder(orderStatus, OrderStatusModel.LISTO, DomainConstants.ORDER_NOT_READY_TO_DELIVER);
    }

    public void validateDataMarkCancelled(Long orderCustomer, Long customerId, OrderStatusModel orderStatus) {
        validateOrderByCustomer(orderCustomer, customerId);
        validateOrderCancelled(orderStatus);
        validateStatusOrder(orderStatus, OrderStatusModel.PENDIENTE, DomainConstants.ORDER_CANNOT_BE_CANCELLED_AFTER_PREPARATION);
    }


}
