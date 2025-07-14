package com.microservice.foodcourt.domain.api;

import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;

public interface IOrderServicePort {

    void saveOrder(OrderModel orderModel);
    PageResult<OrderModel> getOrders(Integer page, Integer size, OrderStatusModel status);

}
