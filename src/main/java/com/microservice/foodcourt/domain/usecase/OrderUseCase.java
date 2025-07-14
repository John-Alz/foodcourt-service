package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IOrderServicePort;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.spi.IUserSessionPort;

import java.util.List;

public class OrderUseCase implements IOrderServicePort  {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IUserSessionPort userSessionPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, 
                        IRestaurantPersistencePort restaurantPersistencePort, 
                        IDishPersistencePort dishPersistencePort,
                        IUserSessionPort userSessionPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.userSessionPort = userSessionPort;
    }

    @Override
    public void saveOrder(OrderModel orderModel) {
        Long loggedUserId = userSessionPort.getUserId();
        orderModel.setCustomerId(loggedUserId);
        List<OrderStatusModel> orderStatus = List.of(
                OrderStatusModel.PENDIENTE,
                OrderStatusModel.PREPARACION,
                OrderStatusModel.LISTO
        );
        orderPersistencePort.existsOrderInProcessByCustomerId(orderModel.getCustomerId(), orderStatus);
        restaurantPersistencePort.validateExist(orderModel.getRestaurant().getId());
        List<Long> dishesId = orderModel.getDishes()
                        .stream()
                        .map(dish -> dish.getDish().getId())
                        .toList();
        for (Long dishId : dishesId) {
            dishPersistencePort.findById(dishId);
        }
        dishPersistencePort.validateAllDishesBelongToRestaurant(dishesId, orderModel.getRestaurant().getId());
        orderPersistencePort.saveOrder(orderModel);
    }
}
