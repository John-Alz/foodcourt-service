package com.microservice.foodcourt.application.dto.response;

import com.microservice.foodcourt.domain.model.CategoryModel;
import com.microservice.foodcourt.domain.model.RestaurantModel;

import java.math.BigDecimal;

public record DishResponseDto(
         Long id,
         String name,
         CategoryModel category,
         String description,
         BigDecimal price,
         RestaurantModel restaurant,
         String imageUrl,
         boolean active
) {
}
