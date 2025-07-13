package com.microservice.foodcourt.domain.api;

import com.microservice.foodcourt.domain.model.OrderModel;

public interface IOrderServicePort {

    void saveOrder(OrderModel orderModel);

}
