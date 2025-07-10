package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantRulesValidatorTest {

    private RestaurantRulesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RestaurantRulesValidator();
    }

    // --- validateName ---
    @Test
    void validateName_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> validator.validateName(null));
        assertEquals("El nombre es requerido.", exception.getMessage());
    }

    @Test
    void validateName_ShouldThrowRequiredFieldException_WhenIsEmpty() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> validator.validateName("     "));
        assertEquals("El nombre es requerido.", exception.getMessage());
    }

    @Test
    void validateName_ShouldThrowInvalidFieldException_WhenOnlyNumbers() {
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> validator.validateName("123456"));
        assertEquals("El nombre no puede ser formado por solo numeros.", exception.getMessage());
    }

    @Test
    void validateName_ShouldPass_WhenValid() {
        assertDoesNotThrow(() -> validator.validateName("Mi Restaurante"));
    }

    // --- validateNit ---
    @Test
    void validateNit_ShouldThrowRequiredFieldException_WhenEmpty() {
        assertThrows(RequiredFieldException.class, () -> validator.validateNit(""));
    }

    @Test
    void validateNit_ShouldThrowInvalidFieldException_WhenNotNumeric() {
        assertThrows(InvalidFieldException.class, () -> validator.validateNit("ABC123"));
    }

    @Test
    void validateNit_ShouldPass_WhenValid() {
        assertDoesNotThrow(() -> validator.validateNit("123456789"));
    }

    // --- validateAddress ---
    @Test
    void validateAddress_ShouldThrowRequiredFieldException_WhenBlank() {
        assertThrows(RequiredFieldException.class, () -> validator.validateAddress("   "));
    }

    @Test
    void validateAddress_ShouldPass_WhenValid() {
        assertDoesNotThrow(() -> validator.validateAddress("Calle 12 #34-56"));
    }

    // --- validatePhoneNumber ---
    @Test
    void validatePhoneNumber_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> validator.validatePhoneNumber(null));
        assertEquals("El numero de telefono es requerido.", exception.getMessage());
    }

    @Test
    void validatePhoneNumber_ShouldThrowRequiredFieldException_WhenIsEmpty() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> validator.validatePhoneNumber("   "));
        assertEquals("El numero de telefono es requerido.", exception.getMessage());
    }

    @Test
    void validatePhoneNumber_ShouldThrowInvalidFieldException_WhenInvalidFormat() {
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> validator.validatePhoneNumber("abc123"));
        assertEquals("El numero de telefeono tiene un formato invalido, eje: (+573005698325).", exception.getMessage());
    }

    @Test
    void validatePhoneNumber_ShouldPass_WhenValid() {
        assertDoesNotThrow(() -> validator.validatePhoneNumber("+573005698325"));
    }

    // --- validateLogo ---
    @Test
    void validateLogo_ShouldThrowRequiredFieldException_WhenBlank() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> validator.validateLogo(" "));
        assertEquals("El logo es requerido.", exception.getMessage());
    }

    @Test
    void validateLogo_ShouldPass_WhenValid() {
        assertDoesNotThrow(() -> validator.validateLogo("https://logo.com/image.png"));
    }

    // --- validateOwnerId ---
    @Test
    void validateOwnerId_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> validator.validateOwnerId(null));
        assertEquals("El Id del propietario es requerido.", exception.getMessage());
    }

    @Test
    void validateOwnerId_ShouldPass_WhenValid() {
        assertDoesNotThrow(() -> validator.validateOwnerId(5L));
    }

    // --- validateRestaurantData ---
    @Test
    void validateRestaurantData_ShouldPass_WhenAllFieldsValid() {
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setName("Tacos Locos");
        restaurant.setNit("123456789");
        restaurant.setAddress("Cra 45 #66-78");
        restaurant.setPhoneNumber("+573005698325");
        restaurant.setUrlLogo("https://logo.com/tacos.png");
        restaurant.setOwnerId(100L);

        assertDoesNotThrow(() -> validator.validateRestaurantData(restaurant));
    }
}
