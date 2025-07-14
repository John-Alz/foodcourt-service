package com.microservice.foodcourt.application.dto.response;

public record DishOrderResponseDto(
         DishResponseDto dish,
         Integer amount
) {
}
