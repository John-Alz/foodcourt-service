package com.microservice.foodcourt.domain.api;

import com.microservice.foodcourt.domain.model.DishModel;

public interface IDishServicePort {

    void saveDish(DishModel dishModel);
    void updateDish(Long id, DishModel updateDishModel);


}
