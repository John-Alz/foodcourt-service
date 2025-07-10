package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.RestaurantModel;

public interface IRestaurantPersistencePort {

    void saveRestaurant(RestaurantModel restaurantModel);
    void validateExist(Long id);
    void validateRestaurantOwnership(Long restaurantId, Long userId);
}
