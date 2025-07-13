package com.microservice.foodcourt.application.dto.request;

import java.util.List;

public record OrderRequestDto(
        Long restaurantId,
        List<DishAmountRequest> dishes
) {
}
