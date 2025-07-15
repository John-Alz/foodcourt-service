package com.microservice.foodcourt.infrastructure.input.rest;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.RestaurantResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IRestaurantHandler;
import com.microservice.foodcourt.domain.model.PageResult;
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
@Tag(name = "Restaurantes", description = "Endpoints de gestion de restaurantes.")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Crear un nuevo restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Error de validacion", content = @Content),
    })
    @PostMapping()
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<SaveMessageResponse> saveRestaurant(@RequestBody RestaurantRequestDto restaurantRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantHandler.saveRestaurant(restaurantRequestDto));
    }

    @GetMapping("/{restaurantId}/belongs-to/{ownerId}")
    public ResponseEntity<Void> isOwnerOfRestaurant(@PathVariable Long restaurantId, @PathVariable Long ownerId) {
        restaurantHandler.validateRestaurantOwnership(restaurantId, ownerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("create-employee")
    public ResponseEntity<Object> createEmployee(@RequestParam Long userId, @RequestParam Long restaurantId) {
        restaurantHandler.createEmployee(userId, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PageResult<RestaurantResponseDto>> getRestaurants(Integer page, Integer size) {
        PageResult<RestaurantResponseDto> restaurantResponseDtoPageResult = restaurantHandler.getRestaurants(page, size);
        return ResponseEntity.ok(restaurantResponseDtoPageResult);
    }

}
