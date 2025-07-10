package com.microservice.foodcourt.infrastructure.configuration;

import com.microservice.foodcourt.domain.api.IRestaurantServicePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.usecase.RestaurantUseCase;
import com.microservice.foodcourt.domain.validation.RestaurantRulesValidator;
import com.microservice.foodcourt.infrastructure.clients.UserClient;
import com.microservice.foodcourt.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final UserClient userClient;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper, userClient);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), restaurantRulesValidator());
    }

    @Bean
    public RestaurantRulesValidator restaurantRulesValidator() {
        return new RestaurantRulesValidator();
    }

}
