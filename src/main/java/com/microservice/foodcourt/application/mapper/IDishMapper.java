package com.microservice.foodcourt.application.mapper;

import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.request.DishUpdateRequestDto;
import com.microservice.foodcourt.application.dto.response.DishResponseDto;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishMapper {

    @Mapping(target = "active", constant = "true")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "restaurantId", target = "restaurant.id")
    DishModel requestToModel(DishRequestDto dishRequestDto);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "id", ignore = true)
    DishModel updateRequestToModel(DishUpdateRequestDto dishUpdateRequestDto);

    PageResult<DishResponseDto> modelListToResponseList(PageResult<DishModel> dishModelList);


}
