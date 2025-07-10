package com.microservice.foodcourt.infrastructure.exceptionhandler;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<ExceptionRespnse> handleRequiredFieldException(RequiredFieldException e) {
        return ResponseEntity.badRequest().body(new ExceptionRespnse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<ExceptionRespnse> handleInvalidFieldException(InvalidFieldException e) {
        return ResponseEntity.badRequest().body(new ExceptionRespnse(e.getMessage(), LocalDateTime.now()));
    }

}
