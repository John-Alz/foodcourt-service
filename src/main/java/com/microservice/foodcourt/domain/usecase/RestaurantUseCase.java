package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IRestaurantServicePort;
import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.utils.DomainConstants;
import com.microservice.foodcourt.domain.validation.RestaurantRulesValidator;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantRulesValidator restaurantRulesValidator;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, RestaurantRulesValidator restaurantRulesValidator) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantRulesValidator = restaurantRulesValidator;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        restaurantRulesValidator.validateRestaurantData(restaurantModel);
        restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    @Override
    public void validateRestaurantOwnership(Long restaurantId, Long userId) {
        restaurantPersistencePort.validateRestaurantOwnership(restaurantId, userId);
    }

    @Override
    public PageResult<RestaurantModel> getRestaurants(Integer page, Integer size) {
        if (page < DomainConstants.PAGE_MIN) throw new InvalidPaginationParameterException(DomainConstants.INVALID_PAGE);
        if (size < DomainConstants.SIZE_MIN) throw new InvalidPaginationParameterException(DomainConstants.INVALID_SIZE);
        return restaurantPersistencePort.getRestaurants(page, size);
    }


}
