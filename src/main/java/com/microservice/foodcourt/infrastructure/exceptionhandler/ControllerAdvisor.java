package com.microservice.foodcourt.infrastructure.exceptionhandler;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.exception.UnauthorizedException;
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

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ExceptionRespnse> handleNoDataFoundException(NoDataFoundException e) {
        return ResponseEntity.badRequest().body(new ExceptionRespnse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionRespnse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.badRequest().body(new ExceptionRespnse("No tienes permiso para editar este plato.", LocalDateTime.now()));
    }

}
