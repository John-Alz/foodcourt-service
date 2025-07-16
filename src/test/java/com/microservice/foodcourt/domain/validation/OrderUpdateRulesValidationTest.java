package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.DataFoundException;
import com.microservice.foodcourt.domain.exception.InvalidOrderStatusException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderUpdateRulesValidationTest {

    private OrderUpdateRulesValidation validator;

    @BeforeEach
    void setUp() {
        validator = new OrderUpdateRulesValidation();
    }

    @Test
    void validateAlreadyAssignedToEmployee_ShouldThrow_WhenAlreadyAssigned() {
        assertThrows(DataFoundException.class, () ->
                validator.validateAlreadyAssignedToEmployee(true)
        );
    }

    @Test
    void validateAlreadyAssignedToEmployee_ShouldNotThrow_WhenNotAssigned() {
        assertDoesNotThrow(() ->
                validator.validateAlreadyAssignedToEmployee(false)
        );
    }

    @Test
    void validateRestaurantByEmployee_ShouldThrow_WhenIdsDoNotMatch() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateRestaurantByEmployee(1L, 2L)
        );
    }

    @Test
    void validateRestaurantByEmployee_ShouldNotThrow_WhenIdsMatch() {
        assertDoesNotThrow(() ->
                validator.validateRestaurantByEmployee(1L, 1L)
        );
    }

    @Test
    void validateAssociatedChef_ShouldThrow_WhenChefIsAssigned() {
        assertThrows(DataFoundException.class, () ->
                validator.validateAssociatedChef(10L)
        );
    }

    @Test
    void validateAssociatedChef_ShouldNotThrow_WhenChefIsNull() {
        assertDoesNotThrow(() ->
                validator.validateAssociatedChef(null)
        );
    }

    @Test
    void validateAssignedChef_ShouldThrow_WhenChefIsNull() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateAssignedChef(null)
        );
    }

    @Test
    void validateAssignedChef_ShouldNotThrow_WhenChefIsAssigned() {
        assertDoesNotThrow(() ->
                validator.validateAssignedChef(1L)
        );
    }

    @Test
    void validateOrderByChef_ShouldThrow_WhenIdsDoNotMatch() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateOrderByChef(1L, 2L)
        );
    }

    @Test
    void validateOrderByChef_ShouldNotThrow_WhenIdsMatch() {
        assertDoesNotThrow(() ->
                validator.validateOrderByChef(1L, 1L)
        );
    }

    @Test
    void validateStatusOrder_ShouldThrow_WhenStatusMismatch() {
        assertThrows(InvalidOrderStatusException.class, () ->
                validator.validateStatusOrder(OrderStatusModel.PREPARACION, OrderStatusModel.LISTO, "Mensaje de error")
        );
    }

    @Test
    void validateStatusOrder_ShouldNotThrow_WhenStatusMatch() {
        assertDoesNotThrow(() ->
                validator.validateStatusOrder(OrderStatusModel.LISTO, OrderStatusModel.LISTO, "")
        );
    }

    @Test
    void validateDataUpdate_ShouldThrow_WhenAlreadyAssigned() {
        assertThrows(DataFoundException.class, () ->
                validator.validateDataUpdate(true, null, 1L, 1L)
        );
    }

    @Test
    void validateDataMarkReady_ShouldThrow_WhenStatusIsNotPreparacion() {
        assertThrows(InvalidOrderStatusException.class, () ->
                validator.validateDataMarkReady(1L, 1L, 1L, 1L, OrderStatusModel.PENDIENTE)
        );
    }

    @Test
    void validateDataMarkDelivered_ShouldThrow_WhenStatusIsNotListo() {
        assertThrows(InvalidOrderStatusException.class, () ->
                validator.validateDataMarkDelivered(1L, 1L, 1L, 1L, OrderStatusModel.PREPARACION)
        );
    }

    @Test
    void validateDataMarkDelivered_ShouldNotThrow_WhenValidData() {
        assertDoesNotThrow(() ->
                validator.validateDataMarkDelivered(1L, 1L, 1L, 1L, OrderStatusModel.LISTO)
        );
    }

    @Test
    void validateOrderCancelled_ShouldThrow_WhenOrderIsCancelled() {
        assertThrows(InvalidOrderStatusException.class, () ->
                validator.validateOrderCancelled(OrderStatusModel.CANCELADO)
        );
    }

    @Test
    void validateOrderCancelled_ShouldNotThrow_WhenOrderIsNotCancelled() {
        assertDoesNotThrow(() ->
                validator.validateOrderCancelled(OrderStatusModel.PENDIENTE)
        );
    }

    @Test
    void validateOrderByCustomer_ShouldThrow_WhenCustomerIdDoesNotMatch() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateOrderByCustomer(1L, 2L)
        );
    }

    @Test
    void validateOrderByCustomer_ShouldNotThrow_WhenCustomerIdMatches() {
        assertDoesNotThrow(() ->
                validator.validateOrderByCustomer(1L, 1L)
        );
    }

    @Test
    void validateDataUpdate_ShouldThrow_WhenRestaurantDoesNotMatch() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateDataUpdate(false, null, 1L, 2L)
        );
    }

    @Test
    void validateDataUpdate_ShouldThrow_WhenChefIsAlreadyAssigned() {
        assertThrows(DataFoundException.class, () ->
                validator.validateDataUpdate(false, 99L, 1L, 1L)
        );
    }

    @Test
    void validateDataUpdate_ShouldNotThrow_WhenValid() {
        assertDoesNotThrow(() ->
                validator.validateDataUpdate(false, null, 1L, 1L)
        );
    }

    @Test
    void validateDataMarkReady_ShouldNotThrow_WhenValidData() {
        assertDoesNotThrow(() ->
                validator.validateDataMarkReady(1L, 1L, 1L, 1L, OrderStatusModel.PREPARACION)
        );
    }

    @Test
    void validateDataMarkCancelled_ShouldThrow_WhenCustomerDoesNotMatch() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateDataMarkCancelled(1L, 2L, OrderStatusModel.PENDIENTE)
        );
    }

    @Test
    void validateDataMarkCancelled_ShouldThrow_WhenOrderAlreadyCancelled() {
        assertThrows(InvalidOrderStatusException.class, () ->
                validator.validateDataMarkCancelled(1L, 1L, OrderStatusModel.CANCELADO)
        );
    }

    @Test
    void validateDataMarkCancelled_ShouldThrow_WhenOrderIsNotPending() {
        assertThrows(InvalidOrderStatusException.class, () ->
                validator.validateDataMarkCancelled(1L, 1L, OrderStatusModel.PREPARACION)
        );
    }

    @Test
    void validateDataMarkCancelled_ShouldNotThrow_WhenOrderIsPendingAndCustomerMatches() {
        assertDoesNotThrow(() ->
                validator.validateDataMarkCancelled(1L, 1L, OrderStatusModel.PENDIENTE)
        );
    }

}
