package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import com.microservice.foodcourt.domain.model.CategoryModel;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DishRulesValidationTest {

    private DishRulesValidation dishRulesValidation;

    private DishModel dishModel;
    private CategoryModel categoryModel;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        dishRulesValidation = new DishRulesValidation();
        categoryModel = new CategoryModel();
        categoryModel.setId(1L);

        restaurantModel = new RestaurantModel();
        restaurantModel.setId(10L);

        dishModel = new DishModel();
        dishModel.setId(100L);
        dishModel.setName("Pizza Napolitana");
        dishModel.setCategory(categoryModel);
        dishModel.setDescription("Delicious Italian pizza");
        dishModel.setPrice(new BigDecimal("45000"));
        dishModel.setRestaurant(restaurantModel);
        dishModel.setImageUrl("https://image.com/pizza.jpg");
        dishModel.setActive(true);
    }

    @Test
    void validateName_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateName(null));
        assertEquals("El nombre es requerido.", exception.getMessage());
    }

    @Test
    void validateName_ShouldThrowRequiredFieldException_WhenEmpty() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateName("   "));
        assertEquals("El nombre es requerido.", exception.getMessage());
    }


    @Test
    void validatePrice_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validatePrice(null));
        assertEquals("El precio es requerido.", exception.getMessage());
    }

    @Test
    void validatePrice_ShouldThrowInvalidFieldException_WhenInvalidFormat() {
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> dishRulesValidation.validatePrice(BigDecimal.valueOf(-1000)));
        assertEquals("El precio debe ser numero entero positivo mayor a cero.", exception.getMessage());
    }

    @Test
    void validateDescription_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateDescription(null));
        assertEquals("La descripcion es requerida.", exception.getMessage());
    }

    @Test
    void validateDescription_ShouldThrowRequiredFieldException_WhenIsEmpty() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateDescription("   "));
        assertEquals("La descripcion es requerida.", exception.getMessage());
    }

    @Test
    void validateImage_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateUrlImge(null));
        assertEquals("La imagen es requerida.", exception.getMessage());
    }

    @Test
    void validateImage_ShouldThrowRequiredFieldException_WhenIsEmpty() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateUrlImge("   "));
        assertEquals("La imagen es requerida.", exception.getMessage());
    }

    @Test
    void validateCategory_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateCategory(null));
        assertEquals("El id de categoria es requerido.", exception.getMessage());
    }

    @Test
    void validateRestaurant_ShouldThrowRequiredFieldException_WhenNull() {
        RequiredFieldException exception = assertThrows(RequiredFieldException.class, () -> dishRulesValidation.validateRestaurant(null));
        assertEquals("El id de restaurante es requerido.", exception.getMessage());
    }

    @Test
    void validateDishData_ShouldPass_WhenAllFieldsValid() {
        assertDoesNotThrow(() -> dishRulesValidation.validateDishData(dishModel));
    }

}














