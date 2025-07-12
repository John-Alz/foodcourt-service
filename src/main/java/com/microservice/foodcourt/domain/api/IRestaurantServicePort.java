package com.microservice.foodcourt.domain.api;

import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.model.RestaurantModel;

public interface IRestaurantServicePort {

    void saveRestaurant(RestaurantModel restaurantModel);

    void validateRestaurantOwnership(Long restaurantId, Long userId);

    PageResult<RestaurantModel> getRestaurants(Integer page, Integer size);



}
