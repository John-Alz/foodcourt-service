package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.PageResult;

import java.util.List;

public interface IDishPersistencePort {

    void saveDish(DishModel dishModel);
    DishModel findById(Long id);
    void validateAllDishesBelongToRestaurant(List<Long> dishesId, Long restaurantId);
    PageResult<DishModel> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId);



}
