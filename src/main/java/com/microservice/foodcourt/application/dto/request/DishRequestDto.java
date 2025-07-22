package com.microservice.foodcourt.application.dto.request;

import java.math.BigDecimal;

public record DishRequestDto(
         String name,
         BigDecimal price,
         String description,
         String imageUrl,
         Long categoryId,
         Long restaurantId
) {
}
