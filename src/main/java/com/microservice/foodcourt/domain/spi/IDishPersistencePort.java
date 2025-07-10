package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.DishModel;

public interface IDishPersistencePort {

    void saveDish(DishModel dishModel);
    DishModel findById(Long id);


}
