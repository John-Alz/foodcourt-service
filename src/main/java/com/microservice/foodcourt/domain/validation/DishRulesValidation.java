package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.utils.DomainConstants;

import java.math.BigDecimal;

public class DishRulesValidation {


    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_NAME);
        }
    }

    public void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_PRICE);
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidFieldException(DomainConstants.INVALID_PRICE);
        }
    }

    public void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_DESCRIPTION);
        }
    }

    public void validateUrlImge(String urlImage) {
        if (urlImage == null || urlImage.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_URLIMAGE);
        }
    }

    public void validateCategory(Long category) {
        if (category == null) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_CATEGORY);
        }
    }

    public void validateRestaurant(Long restaurant) {
        if (restaurant == null) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_RESTAURANT);
        }
    }

    public void validateDishData(DishModel dish) {
        validateName(dish.getName());
        validatePrice(dish.getPrice());
        validateDescription(dish.getDescription());
        validateUrlImge(dish.getImageUrl());
        validateCategory(dish.getCategory().getId());
        validateRestaurant(dish.getRestaurant().getId());
    }

}
