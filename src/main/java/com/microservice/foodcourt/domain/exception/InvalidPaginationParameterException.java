package com.microservice.foodcourt.domain.exception;

public class InvalidPaginationParameterException extends RuntimeException {
    public InvalidPaginationParameterException(String message) {
        super(message);
    }
}
