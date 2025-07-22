package com.microservice.foodcourt.application.dto.response;

import java.time.LocalDateTime;

public record SaveMessageResponse(String message, LocalDateTime time) {
}
