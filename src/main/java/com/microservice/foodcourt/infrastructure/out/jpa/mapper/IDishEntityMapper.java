package com.microservice.foodcourt.infrastructure.out.jpa.mapper;

import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {

    DishEntity toEntity(DishModel dishModel);
    DishModel toModel(DishEntity dishEntity);

    List<DishModel> entityListToModelList(List<DishEntity> dishEntities);

}
