package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.model.OrderModel;

public interface IOrderPersistencePort {

    void saveOrder(OrderModel orderModel);

}
