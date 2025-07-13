package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.model.DishOrderModel;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.*;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IDishRepository;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IOrderRepository;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OrderJpaMapper implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IDishRepository dishRepository;
    private final IRestaurantRepository restaurantRepository;

    @Override
    public void saveOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(orderModel);
        List<DishOrderEntity> dishOrders = new ArrayList<>();
        for (DishOrderModel dishOrderModel : orderModel.getDishes()) {
            Long dishId = dishOrderModel.getDish().getId();
            DishEntity dish = dishRepository.findById(dishId).orElse(null);
            if (dish == null) {
                throw new NoDataFoundException("No se escontro el plato");
            }
            DishOrderEntityId dishOrderEntityId = new DishOrderEntityId(null, dish.getId());
            DishOrderEntity  dishOrder = new DishOrderEntity();
            dishOrder.setId(dishOrderEntityId);
            dishOrder.setDish(dish);
            dishOrder.setOrder(orderEntity);
            dishOrder.setAmount(dishOrderModel.getAmount());
            dishOrders.add(dishOrder);
        }
        orderEntity.setDishes(dishOrders);
        orderRepository.save(orderEntity);
    }
}
