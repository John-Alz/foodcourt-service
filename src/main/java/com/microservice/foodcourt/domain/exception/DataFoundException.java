package com.microservice.foodcourt.domain.exception;

public class DataFoundException extends RuntimeException {
    public DataFoundException(String message) {
        super(message);
    }
}
