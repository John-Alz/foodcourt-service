package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;

import java.util.List;

public interface IOrderPersistencePort {

    void saveOrder(OrderModel orderModel);

    void existsOrderInProcessByCustomerId(Long customerId, List<OrderStatusModel> status);

    PageResult<OrderModel> getOrders(Integer page, Integer size, Long restaurantId, OrderStatusModel status);


}
