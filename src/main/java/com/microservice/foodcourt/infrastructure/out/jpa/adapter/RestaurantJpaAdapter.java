package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.infrastructure.clients.UserClient;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final UserClient userClient;

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        userClient.validateUser(restaurantModel.getOwnerId(), "OWNER");
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurantModel));
    }
}
