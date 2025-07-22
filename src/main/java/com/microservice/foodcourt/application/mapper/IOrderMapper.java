package com.microservice.foodcourt.application.mapper;

import com.microservice.foodcourt.application.dto.request.OrderRequestDto;
import com.microservice.foodcourt.application.dto.response.OrderResponseDto;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = "spring",
        uses = IDishOrderMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IOrderMapper {

    @Mapping(source = "restaurantId", target = "restaurant.id")
    @Mapping(source = "dishes", target = "dishes")
    OrderModel requestToModel(OrderRequestDto orderRequestDto);

    PageResult<OrderResponseDto> modelListToResponseList(PageResult<OrderModel> orderModels);

}
