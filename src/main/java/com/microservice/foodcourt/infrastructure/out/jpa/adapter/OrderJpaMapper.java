package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.model.DishOrderModel;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.infrastructure.exception.CustomerHasOngoingOrderException;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.*;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IDishRepository;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IOrderRepository;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Override
    public void existsOrderInProcessByCustomerId(Long customerId, List<OrderStatusModel> status) {
        List<OrderStatusEntity> orderStatusEntities = orderEntityMapper.toOrderStatusList(status);
        if (orderRepository.existsByCustomerIdAndStatuses(customerId, orderStatusEntities)) {
            throw new CustomerHasOngoingOrderException();
        }
    }

    @Override
    public PageResult<OrderModel> getOrders(Integer page, Integer size, Long restaurantId, OrderStatusModel status) {
        Pageable paging = PageRequest.of(page, size);
        Page<OrderEntity> orderPage = orderRepository.findAllByRestaurant(restaurantId, orderEntityMapper.toOrderStatusEntity(status), paging);
        List<OrderModel> orderList = orderEntityMapper.listEntityToListModel(orderPage.getContent());
        return new PageResult<>(
                orderList,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );
    }

    @Override
    public OrderModel getOrderById(Long orderId) {
        OrderEntity orderFound = orderRepository.findById(orderId).orElse(null);
        if (orderFound == null) throw new NoDataFoundException("No existe una orden con ese id.");
        return orderEntityMapper.toModel(orderFound);
    }

    @Override
    public void updateOrder(OrderModel orderModel) {
        orderRepository.save(orderEntityMapper.toEntityWithoutDishestoEntity(orderModel));
    }

    @Override
    public boolean isOrderAlreadyAssignedToEmployee(Long employeeId, Long orderId) {
        return orderRepository.existsByIdAndChefId(employeeId, orderId);
    }



}
