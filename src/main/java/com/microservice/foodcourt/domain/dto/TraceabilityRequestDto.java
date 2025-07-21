package com.microservice.foodcourt.domain.dto;

import java.time.LocalDateTime;

public record TraceabilityRequestDto(
        Long orderId,
        Long customerId,
        String emailCustomer,
        String previousStatus,
        String newStatus,
        Long employeeId,
        String employeeEmail,
        Long restaurantId
) {
}
