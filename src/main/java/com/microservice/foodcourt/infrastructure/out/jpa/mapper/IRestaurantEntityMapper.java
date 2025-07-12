package com.microservice.foodcourt.infrastructure.out.jpa.mapper;

import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRestaurantEntityMapper {

    RestaurantModel toModel(RestaurantEntity restaurantEntity);

    RestaurantEntity toEntity(RestaurantModel restaurantModel);

    List<RestaurantModel> listEntityToListModel(List<RestaurantEntity> restaurantEntities);

}
