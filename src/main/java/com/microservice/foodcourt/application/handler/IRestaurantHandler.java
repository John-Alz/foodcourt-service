package com.microservice.foodcourt.application.handler;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.RestaurantResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.domain.model.PageResult;

public interface IRestaurantHandler {

    SaveMessageResponse saveRestaurant(RestaurantRequestDto restaurantRequestDto);

    void validateRestaurantOwnership(Long restaurantId, Long ownerId);

    PageResult<RestaurantResponseDto> getRestaurants(Integer page, Integer size);

}
