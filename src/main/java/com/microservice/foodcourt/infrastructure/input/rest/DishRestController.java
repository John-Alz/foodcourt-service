package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.DishChangeStatusDto;
import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.request.DishUpdateRequestDto;
import com.microservice.foodcourt.application.dto.response.DishResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IDishHandler;
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
@RequestMapping("api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Endpoints de gestión de platos.")
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(
            summary = "Crear un nuevo plato.",
            description = "Permite al propietario de un restaurante registrar un nuevo plato en su menú."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado exitosamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validación al crear el plato.", content = @Content),
    })
    @PostMapping()
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_OWNER)
    public ResponseEntity<SaveMessageResponse> saveDish(@RequestBody DishRequestDto dishRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishHandler.saveDish(dishRequestDto));
    }

    @Operation(
            summary = "Actualizar un plato existente.",
            description = "Permite al propietario actualizar los datos de un plato de su restaurante."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado exitosamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado o error de validación.", content = @Content),
    })
    @PutMapping("/{id}")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_OWNER)
    public ResponseEntity<SaveMessageResponse> updateDish(@PathVariable Long id, @RequestBody DishUpdateRequestDto dishUpdateRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(dishHandler.updateDish(id, dishUpdateRequestDto));
    }

    @Operation(
            summary = "Cambiar el estado de un plato.",
            description = "Permite habilitar o deshabilitar la visibilidad de un plato en el menú del restaurante."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del plato actualizado exitosamente.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado o error de validación.", content = @Content),
    })
    @PutMapping("/{id}/status")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_OWNER)
    public ResponseEntity<SaveMessageResponse> changeDishStatus(@PathVariable Long id, @RequestBody DishChangeStatusDto dishChangeStatusDto) {
        return ResponseEntity.status(HttpStatus.OK).body(dishHandler.changeDishStatus(id, dishChangeStatusDto));
    }

    @Operation(
            summary = "Obtener menú del restaurante.",
            description = "Permite a un cliente visualizar los platos disponibles de un restaurante, con paginación y filtrado por categoría."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de platos obtenido exitosamente.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Solo usuarios con rol CUSTOMER pueden acceder.", content = @Content),
    })
    @GetMapping("/{restaurantId}/menu")
    @PreAuthorize(InfrastructureConstants.HAS_ROLE_CUSTOMER)
    public ResponseEntity<PageResult<DishResponseDto>> getDishes(
            @PathVariable Long restaurantId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId) {
        PageResult<DishResponseDto> dishResponseDtoPageResult = dishHandler.getDishes(page, size, restaurantId, categoryId);
        return ResponseEntity.ok(dishResponseDtoPageResult);
    }
}

