package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;

import java.util.List;

public interface IOrderPersistencePort {

    void saveOrder(OrderModel orderModel);

    void existsOrderInProcessByCustomerId(Long customerId, List<OrderStatusModel> status);

}
