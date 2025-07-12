package com.microservice.foodcourt.domain.api;

import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.PageResult;

public interface IDishServicePort {

    void saveDish(DishModel dishModel);
    void updateDish(Long id, DishModel updateDishModel);
    void changeDishStatus(Long id,boolean status);
    PageResult<DishModel> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId);


}
