package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name = "Plaza de comidas", description = "Endpoints de gestion de plaza de comidas.")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Crear un nuevo restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validacion", content = @Content),
    })
    @PostMapping()
    public ResponseEntity<SaveMessageResponse> saveRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantHandler.saveRestaurant(restaurantRequestDto));
    }

}
