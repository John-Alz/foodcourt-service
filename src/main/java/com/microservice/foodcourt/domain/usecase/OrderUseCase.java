package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IOrderServicePort;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;

public class OrderUseCase implements IOrderServicePort  {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, IRestaurantPersistencePort restaurantPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveOrder(OrderModel orderModel) {
        restaurantPersistencePort.validateExist(orderModel.getRestaurant().getId());
        orderPersistencePort.saveOrder(orderModel);
    }
}
