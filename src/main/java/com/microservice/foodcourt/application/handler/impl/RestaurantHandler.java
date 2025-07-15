package com.microservice.foodcourt.application.handler.impl;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.RestaurantResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IRestaurantHandler;
import com.microservice.foodcourt.application.mapper.IRestaurantMapper;
import com.microservice.foodcourt.domain.api.IRestaurantServicePort;
import com.microservice.foodcourt.domain.model.PageResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantMapper restaurantMapper;

    @Override
    public SaveMessageResponse saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        restaurantServicePort.saveRestaurant(restaurantMapper.requestToModel(restaurantRequestDto));
        return new SaveMessageResponse("Restaurante creado.", LocalDateTime.now());
    }

    @Override
    public void validateRestaurantOwnership(Long restaurantId, Long ownerId) {
        restaurantServicePort.validateRestaurantOwnership(restaurantId, ownerId);
    }

    @Override
    public void createEmployee(Long userId, Long restaurantId) {
        restaurantServicePort.createEmployee(userId, restaurantId);
    }

    @Override
    public PageResult<RestaurantResponseDto> getRestaurants(Integer page, Integer size) {
        return restaurantMapper.modelListToResponseList(restaurantServicePort.getRestaurants(page, size));
    }


}
