package com.microservice.foodcourt.infrastructure.exceptionhandler;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import com.microservice.foodcourt.infrastructure.exception.CustomerHasOngoingOrderException;
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
        return ResponseEntity.badRequest().body(new ExceptionRespnse("Acción no permitida: el restaurante no es de tu propiedad.", LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidPaginationParameterException.class)
    public ResponseEntity<ExceptionRespnse> handleInvalidPaginationParameterException(InvalidPaginationParameterException e) {
        return ResponseEntity.badRequest().body(new ExceptionRespnse(e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(CustomerHasOngoingOrderException.class)
    public ResponseEntity<ExceptionRespnse> handleCustomerHasOngoingOrderException(CustomerHasOngoingOrderException e) {
        return ResponseEntity.badRequest().body(new ExceptionRespnse("Ya existe un pedido activo para este cliente. Espere a que finalice para crear uno nuevo.", LocalDateTime.now()));
    }

}
