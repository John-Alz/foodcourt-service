package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.PageResult;

public interface IDishPersistencePort {

    void saveDish(DishModel dishModel);
    DishModel findById(Long id);
    Long getUserId();
    PageResult<DishModel> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId);



}
