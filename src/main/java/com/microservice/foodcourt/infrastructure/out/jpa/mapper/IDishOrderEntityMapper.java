package com.microservice.foodcourt.infrastructure.out.jpa.mapper;

import com.microservice.foodcourt.domain.model.DishOrderModel;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.DishOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishOrderEntityMapper {

    @Mapping(target = "order", ignore = true)
    DishOrderModel toModel(DishOrderEntity entity);

    DishOrderEntity toEntity(DishOrderModel model);
}
