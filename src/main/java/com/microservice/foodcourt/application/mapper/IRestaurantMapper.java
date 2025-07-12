package com.microservice.foodcourt.application.mapper;

import com.microservice.foodcourt.application.dto.request.RestaurantRequestDto;
import com.microservice.foodcourt.application.dto.response.RestaurantResponseDto;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRestaurantMapper {

    RestaurantModel requestToModel(RestaurantRequestDto restaurantRequestDto);

    PageResult<RestaurantResponseDto> modelListToResponseList(PageResult<RestaurantModel> restauranstList);

}
