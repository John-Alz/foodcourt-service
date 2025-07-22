package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.OrderCodeVerificationRequestDto;
import com.microservice.foodcourt.application.dto.request.OrderRequestDto;
import com.microservice.foodcourt.application.dto.response.OrderResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IOrderHandler;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.infrastructure.utils.InfrastructureConstants;
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

    @Operation(
            summary = "Crear una nueva orden.",
            description = "Permite a un cliente crear una nueva orden con los platos seleccionados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud.", content = @Content),
    })
    @PostMapping
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_CUSTOMER)
    public ResponseEntity<SaveMessageResponse> saveOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderHandler.saveOrder(orderRequestDto));
    }

    @Operation(
            summary = "Obtener órdenes por estado.",
            description = "Permite a un empleado ver las órdenes en estado PENDIENTE, PREPARACION, LISTO o ENTREGADO de manera paginada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes obtenidas exitosamente.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado para este rol.", content = @Content)
    })
    @GetMapping
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_EMPLOYEE)
    public ResponseEntity<PageResult<OrderResponseDto>> getOrders(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) OrderStatusModel status) {
        PageResult<OrderResponseDto> orderResponseDtoPageResult = orderHandler.getOrders(page, size, status);
        return ResponseEntity.ok(orderResponseDtoPageResult);
    }

    @Operation(
            summary = "Asignar una orden a un empleado e iniciar preparación.",
            description = "Cambia el estado de la orden a PREPARACION asignándola al empleado autenticado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden asignada y actualizada a preparación.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada o error de validación.", content = @Content),
    })
    @PatchMapping("/{id}/assign")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_EMPLOYEE)
    public ResponseEntity<SaveMessageResponse> updateOrderStatusPreparation(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.startOrderPreparation(id));
    }

    @Operation(
            summary = "Marcar orden como lista.",
            description = "Permite a un empleado cambiar el estado de la orden a LISTO una vez la preparación ha finalizado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden marcada como lista.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada o error de validación.", content = @Content),
    })
    @PatchMapping("/{id}/ready")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_EMPLOYEE)
    public ResponseEntity<SaveMessageResponse> updateOrderStatusReady(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.markOrderAsReady(id));
    }

    @Operation(
            summary = "Marcar orden como entregada.",
            description = "Permite a un empleado marcar una orden como ENTREGADA usando un código de verificación proporcionado por el cliente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden entregada exitosamente.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Código de verificación incorrecto.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada.", content = @Content),
    })
    @PatchMapping("/{id}/deliver")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_EMPLOYEE)
    public ResponseEntity<SaveMessageResponse> updateOrderStatusDelivered(@PathVariable Long id, @RequestBody OrderCodeVerificationRequestDto codeVerificationRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.markOrderAsDelivered(id, codeVerificationRequestDto));
    }

    @Operation(
            summary = "Cancelar una orden.",
            description = "Permite a un cliente cancelar una orden mientras aún esté en estado PENDIENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden cancelada exitosamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada o no se puede cancelar.", content = @Content),
    })
    @PatchMapping("/{id}/cancel")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_CUSTOMER)
    public ResponseEntity<SaveMessageResponse> updateOrderStatusCancel(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderHandler.markOrderAsCancelled(id));
    }
}

