package com.microservice.foodcourt.application.dto.request;

import java.math.BigDecimal;

public record DishUpdateRequestDto(BigDecimal price, String description) {
}
