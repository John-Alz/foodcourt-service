package com.microservice.foodcourt.infrastructure.configuration;

import com.microservice.foodcourt.domain.api.IDishServicePort;
import com.microservice.foodcourt.domain.api.IOrderServicePort;
import com.microservice.foodcourt.domain.api.IRestaurantServicePort;
import com.microservice.foodcourt.domain.spi.ICategoryPersistencePort;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.usecase.DishUseCase;
import com.microservice.foodcourt.domain.usecase.OrderUseCase;
import com.microservice.foodcourt.domain.usecase.RestaurantUseCase;
import com.microservice.foodcourt.domain.validation.DishRulesValidation;
import com.microservice.foodcourt.domain.validation.RestaurantRulesValidator;
import com.microservice.foodcourt.infrastructure.clients.UserClient;
import com.microservice.foodcourt.infrastructure.out.jpa.adapter.CategoryJpaAdapter;
import com.microservice.foodcourt.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.microservice.foodcourt.infrastructure.out.jpa.adapter.OrderJpaMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.ICategoryRepository;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IDishRepository;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IOrderRepository;
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

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    private final ICategoryRepository categoryRepository;

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

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

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public IDishServicePort dishServicePort(IRestaurantPersistencePort restaurantPersistencePort) {
        return new DishUseCase(dishPersistencePort(), categoryPersistencePort(), restaurantPersistencePort(), dishRulesValidation());
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryRepository);
    }

    @Bean
    public DishRulesValidation dishRulesValidation() {
        return new DishRulesValidation();
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaMapper(orderRepository, orderEntityMapper, dishRepository, restaurantRepository);
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(), restaurantPersistencePort());
    }

}

