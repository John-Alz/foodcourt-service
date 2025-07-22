package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.model.RestaurantModel;

public interface IRestaurantPersistencePort {

    void saveRestaurant(RestaurantModel restaurantModel);

    void validateExist(Long id);

    void validateRestaurantOwnership(Long restaurantId, Long userId);

    void createEmployee(Long userId, Long restaurantId);

    PageResult<RestaurantModel> getRestaurants(Integer page, Integer size);

    Long getRestaurantByEmployee(Long employeeId);

}
