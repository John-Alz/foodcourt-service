package com.microservice.foodcourt.application.dto.request;

public record DishAmountRequest(
        Long dishId,
        Integer amount

) {
}
