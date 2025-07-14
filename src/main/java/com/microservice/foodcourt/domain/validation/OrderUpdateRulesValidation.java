package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.DataFoundException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;

public class OrderUpdateRulesValidation {


    public void validateAlreadyAssignedToEmployee(boolean isAlreadyAssigned) {
        if (isAlreadyAssigned) throw new DataFoundException("Ya te asignaste este pedido");
    }

    public void validateRestaurantByEmployee(Long restaurantIdByEmployee, Long restaurantId) {
        if (!restaurantIdByEmployee.equals(restaurantId)) {
            throw new UnauthorizedActionException();
        }
    }

    public void validateAssociatedChef(Long chefId) {
        if (chefId != null) throw new DataFoundException("Ya existe un chef asignado a este pedido");
    }

    public void validateDataUpdate(boolean isAlreadyAssigned, Long chefId, Long restaurantIdByEmployee, Long restaurantId) {
        validateAlreadyAssignedToEmployee(isAlreadyAssigned);
        validateRestaurantByEmployee(restaurantIdByEmployee, restaurantId);
        validateAssociatedChef(chefId);
    }

}
