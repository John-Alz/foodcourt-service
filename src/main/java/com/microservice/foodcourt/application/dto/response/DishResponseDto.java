package com.microservice.foodcourt.application.dto.response;

import com.microservice.foodcourt.domain.model.CategoryModel;

import java.math.BigDecimal;

public record DishResponseDto(
         Long id,
         String name,
         CategoryModel category,
         String description,
         BigDecimal price,
         RestaurantResponseDto restaurant,
         String imageUrl
) {
}
