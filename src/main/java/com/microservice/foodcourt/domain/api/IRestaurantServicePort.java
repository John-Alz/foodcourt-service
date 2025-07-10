package com.microservice.foodcourt.domain.api;

import com.microservice.foodcourt.domain.model.RestaurantModel;

public interface IRestaurantServicePort {

    void saveRestaurant(RestaurantModel restaurantModel);

}
