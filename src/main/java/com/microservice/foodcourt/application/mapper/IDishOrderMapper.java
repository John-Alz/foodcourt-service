package com.microservice.foodcourt.application.mapper;

import com.microservice.foodcourt.application.dto.request.DishAmountRequest;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.DishOrderModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IDishOrderMapper {

    @Mapping(source = "dishId", target = "dish.id")
    @Mapping(source = "amount", target = "amount")
    DishOrderModel toDishOrderModel(DishAmountRequest dto);

    @Named("dishFromId")
    default DishModel mapDishFromId(Long id) {
        DishModel dish = new DishModel();
        dish.setId(id);
        return dish;
    }
}
