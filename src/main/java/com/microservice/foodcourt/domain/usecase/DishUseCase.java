package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IDishServicePort;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.spi.ICategoryPersistencePort;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.validation.DishRulesValidation;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final DishRulesValidation dishRulesValidation;

    public DishUseCase(IDishPersistencePort dishPersistencePort, ICategoryPersistencePort categoryPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, DishRulesValidation dishRulesValidation) {
        this.dishPersistencePort = dishPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishRulesValidation = dishRulesValidation;
    }

    @Override
    public void saveDish(DishModel dishModel) {
        dishRulesValidation.validateDishData(dishModel);
        categoryPersistencePort.existCategory(dishModel.getCategory().getId());
        restaurantPersistencePort.validateExist(dishModel.getRestaurant().getId());
        dishPersistencePort.saveDish(dishModel);
    }
}
