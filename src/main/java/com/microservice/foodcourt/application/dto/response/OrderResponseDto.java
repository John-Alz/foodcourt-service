package com.microservice.foodcourt.application.dto.response;

import com.microservice.foodcourt.domain.model.OrderStatusModel;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Long customerId,
        LocalDateTime date,
        OrderStatusModel status ,
        Long chefId,
        RestaurantResponseDto restaurant,
        List<DishOrderResponseDto> dishes
) {
}
