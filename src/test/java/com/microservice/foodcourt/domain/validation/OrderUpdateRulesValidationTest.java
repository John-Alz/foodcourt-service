package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.DataFoundException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderUpdateRulesValidationTest {

    private OrderUpdateRulesValidation validator;

    @BeforeEach
    void setUp() {
        validator = new OrderUpdateRulesValidation();
    }

    // --- Tests for validateAlreadyAssignedToEmployee ---

    @Test
    void validateAlreadyAssignedToEmployee_shouldThrowException_whenAlreadyAssigned() {
        assertThrows(DataFoundException.class, () -> validator.validateAlreadyAssignedToEmployee(true));
    }

    @Test
    void validateAlreadyAssignedToEmployee_shouldPass_whenNotAssigned() {
        assertDoesNotThrow(() -> validator.validateAlreadyAssignedToEmployee(false));
    }

    // --- Tests for validateRestaurantByEmployee ---

    @Test
    void validateRestaurantByEmployee_shouldThrowException_whenRestaurantMismatch() {
        Long employeeRestaurantId = 1L;
        Long orderRestaurantId = 2L;

        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateRestaurantByEmployee(employeeRestaurantId, orderRestaurantId));
    }

    @Test
    void validateRestaurantByEmployee_shouldPass_whenSameRestaurant() {
        Long restaurantId = 10L;

        assertDoesNotThrow(() ->
                validator.validateRestaurantByEmployee(restaurantId, restaurantId));
    }

    // --- Tests for validateAssociatedChef ---

    @Test
    void validateAssociatedChef_shouldThrowException_whenChefAlreadyAssigned() {
        assertThrows(DataFoundException.class, () -> validator.validateAssociatedChef(5L));
    }

    @Test
    void validateAssociatedChef_shouldPass_whenChefIsNull() {
        assertDoesNotThrow(() -> validator.validateAssociatedChef(null));
    }

    // --- Test for validateDataUpdate (full flow) ---

    @Test
    void validateDataUpdate_shouldPass_whenAllValidationsPass() {
        assertDoesNotThrow(() -> validator.validateDataUpdate(false, null, 1L, 1L));
    }

    @Test
    void validateDataUpdate_shouldThrowException_whenAlreadyAssigned() {
        assertThrows(DataFoundException.class, () ->
                validator.validateDataUpdate(true, null, 1L, 1L));
    }

    @Test
    void validateDataUpdate_shouldThrowException_whenDifferentRestaurants() {
        assertThrows(UnauthorizedActionException.class, () ->
                validator.validateDataUpdate(false, null, 1L, 2L));
    }

    @Test
    void validateDataUpdate_shouldThrowException_whenChefAlreadyAssigned() {
        assertThrows(DataFoundException.class, () ->
                validator.validateDataUpdate(false, 100L, 1L, 1L));
    }
}
