package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.RestaurantResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IRestaurantHandler;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.infrastructure.utils.InfrastructureConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Endpoints de gestión de restaurantes.")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(
            summary = "Crear un nuevo restaurante.",
            description = "Permite a un administrador crear un nuevo restaurante proporcionando los datos necesarios."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado exitosamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validación en los datos de entrada.", content = @Content),
    })
    @PostMapping
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<SaveMessageResponse> saveRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantHandler.saveRestaurant(restaurantRequestDto));
    }

    @Operation(
            summary = "Validar si un usuario es propietario de un restaurante.",
            description = "Verifica que el usuario con el ID proporcionado sea el propietario del restaurante indicado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El usuario es propietario del restaurante.", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario no es propietario del restaurante.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante o usuario no encontrados.", content = @Content),
    })
    @GetMapping("/{restaurantId}/belongs-to/{ownerId}")
    public ResponseEntity<Void> isOwnerOfRestaurant(@PathVariable Long restaurantId, @PathVariable Long ownerId) {
        restaurantHandler.validateRestaurantOwnership(restaurantId, ownerId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Asignar un empleado a un restaurante.",
            description = "Permite asignar un usuario existente como empleado de un restaurante específico."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado asignado exitosamente al restaurante.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos o error de negocio.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario o restaurante no encontrado.", content = @Content),
    })
    @PostMapping("/create-employee")
    public ResponseEntity<Object> createEmployee(@RequestParam Long userId, @RequestParam Long restaurantId) {
        restaurantHandler.createEmployee(userId, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Listar restaurantes disponibles.",
            description = "Obtiene una lista paginada de restaurantes disponibles para los clientes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de restaurantes obtenido correctamente.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo usuarios con rol CUSTOMER pueden acceder.", content = @Content)
    })
    @GetMapping
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_CUSTOMER)
    public ResponseEntity<PageResult<RestaurantResponseDto>> getRestaurants(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        PageResult<RestaurantResponseDto> restaurantResponseDtoPageResult = restaurantHandler.getRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponseDtoPageResult);
    }
}

