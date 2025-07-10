package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.request.DishUpdateRequestDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Endpoints de gestion de platos.")
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(summary = "Crear un nuevo plato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validacion", content = @Content),
    })
    @PostMapping()
    public ResponseEntity<SaveMessageResponse> saveDish(@RequestBody DishRequestDto dishRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dishHandler.saveDish(dishRequestDto));
    }

    @Operation(summary = "Actualizar plato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validacion", content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<SaveMessageResponse> updateDish(@PathVariable Long id, @RequestBody DishUpdateRequestDto dishUpdateRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(dishHandler.updateDish(id, dishUpdateRequestDto));
    }


}
