package com.microservice.foodcourt.application.handler;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;

public interface IRestaurantHandler {

    SaveMessageResponse saveRestaurant(RestaurantRequestDto restaurantRequestDto);

}
