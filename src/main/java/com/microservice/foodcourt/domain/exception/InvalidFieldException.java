package com.microservice.foodcourt.domain.exception;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }
}
