package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.OrderCodeVerificationRequestDto;
import com.microservice.foodcourt.application.dto.request.OrderRequestDto;
import com.microservice.foodcourt.application.dto.response.OrderResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IOrderHandler;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<SaveMessageResponse> saveOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderHandler.saveOrder(orderRequestDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PageResult<OrderResponseDto>> getOrders(Integer page, Integer size, OrderStatusModel status) {
        PageResult<OrderResponseDto> orderResponseDtoPageResult = orderHandler.getOrders(page, size, status);
        return ResponseEntity.ok(orderResponseDtoPageResult);
    }

    @Operation(summary = "Cambiar de estado de la orden.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden actualizada.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validacion", content = @Content),
    })

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<SaveMessageResponse> updateOrderStatusPreparation(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.startOrderPreparation(id));
    }

    @PatchMapping("/{id}/ready")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<SaveMessageResponse> updateOrderStatusReady(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.markOrderAsReady(id));
    }

    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<SaveMessageResponse> updateOrderStatusDelivered(@PathVariable Long id, @RequestBody OrderCodeVerificationRequestDto codeVerificationRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.markOrderAsDelivered(id, codeVerificationRequestDto));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<SaveMessageResponse> updateOrderStatusCancel(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.markOrderAsCancelled(id));
    }

}
