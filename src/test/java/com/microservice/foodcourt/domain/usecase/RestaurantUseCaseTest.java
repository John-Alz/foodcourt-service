package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.validation.RestaurantRulesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class RestaurantUseCaseTest {

    private IRestaurantPersistencePort restaurantPersistencePort;
    private RestaurantRulesValidator restaurantRulesValidator;
    private RestaurantUseCase restaurantUseCase;

    @BeforeEach
    void setUp() {
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        restaurantRulesValidator = mock(RestaurantRulesValidator.class);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, restaurantRulesValidator);
    }

    @Test
    void saveRestaurant_ShouldValidateAndSave() {

        RestaurantModel restaurantModel = new RestaurantModel();

        restaurantUseCase.saveRestaurant(restaurantModel);

        verify(restaurantRulesValidator, times(1)).validateRestaurantData(restaurantModel);
        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurantModel);
        verifyNoMoreInteractions(restaurantRulesValidator, restaurantPersistencePort);
    }
}
