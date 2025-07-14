package com.microservice.foodcourt.application.dto.request;

import com.microservice.foodcourt.domain.model.OrderStatusModel;

public record OrderUpdateRequestDto(
        OrderStatusModel status
) {
}
