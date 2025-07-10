package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IRestaurantServicePort;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.validation.RestaurantRulesValidator;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantRulesValidator restaurantRulesValidator;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, RestaurantRulesValidator restaurantRulesValidator) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantRulesValidator = restaurantRulesValidator;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        restaurantRulesValidator.validateRestaurantData(restaurantModel);
        restaurantPersistencePort.saveRestaurant(restaurantModel);
    }
}
