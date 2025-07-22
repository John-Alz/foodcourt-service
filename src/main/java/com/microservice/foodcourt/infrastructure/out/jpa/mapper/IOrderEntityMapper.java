package com.microservice.foodcourt.infrastructure.out.jpa.mapper;

import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderStatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { IDishOrderEntityMapper.class },
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IOrderEntityMapper {

    OrderEntity toEntity(OrderModel orderModel);

    @Mapping(target = "dishes", ignore = true)
    OrderEntity toEntityWithoutDishestoEntity(OrderModel orderModel);


    OrderModel toModel(OrderEntity orderEntity);

    OrderStatusEntity toOrderStatusEntity(OrderStatusModel orderStatusModel);

    List<OrderStatusEntity> toOrderStatusList(List<OrderStatusModel> orderStatusModels);

    List<OrderModel> listEntityToListModel(List<OrderEntity> orderEntities);

}
