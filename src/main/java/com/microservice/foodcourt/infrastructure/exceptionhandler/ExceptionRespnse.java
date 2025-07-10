package com.microservice.foodcourt.infrastructure.exceptionhandler;

import java.time.LocalDateTime;

public record ExceptionRespnse(String message, LocalDateTime timeStamp) {
}
