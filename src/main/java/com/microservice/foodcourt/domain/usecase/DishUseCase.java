package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IDishServicePort;
import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.spi.ICategoryPersistencePort;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.utils.DomainConstants;
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
        Long userId = dishPersistencePort.getUserId();
        restaurantPersistencePort.validateRestaurantOwnership(dishModel.getRestaurant().getId(), userId);
        dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public void updateDish(Long id, DishModel updateDishModel) {
        DishModel dishFound = dishPersistencePort.findById(id);
        Long restaurantId = dishFound.getRestaurant().getId();
        Long getUserId = dishPersistencePort.getUserId();
        restaurantPersistencePort.validateRestaurantOwnership(restaurantId, getUserId);

        dishFound.setPrice(updateDishModel.getPrice());
        dishFound.setDescription(updateDishModel.getDescription());
        dishPersistencePort.saveDish(dishFound);
    }

    @Override
    public void changeDishStatus(Long id, boolean status) {
        DishModel dishFound = dishPersistencePort.findById(id);
        Long userId = dishPersistencePort.getUserId();
        restaurantPersistencePort.validateRestaurantOwnership(dishFound.getRestaurant().getId(), userId);
        dishFound.setActive(status);
        dishPersistencePort.saveDish(dishFound);
    }

    @Override
    public PageResult<DishModel> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId) {
        if(page < DomainConstants.PAGE_MIN) throw new InvalidPaginationParameterException(DomainConstants.INVALID_PAGE);
        if(size < DomainConstants.SIZE_MIN) throw new InvalidPaginationParameterException(DomainConstants.INVALID_SIZE);
        restaurantPersistencePort.validateExist(restaurantId);
        if (categoryId != null) {
            categoryPersistencePort.existCategory(categoryId);
        }
        return dishPersistencePort.getDishes(page, size, restaurantId, categoryId);
    }

}
