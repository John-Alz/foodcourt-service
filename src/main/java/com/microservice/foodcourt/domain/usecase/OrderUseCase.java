package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IOrderServicePort;
import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.exception.InvalidVerificationCodeException;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.spi.IUserSessionPort;
import com.microservice.foodcourt.domain.utils.DomainConstants;
import com.microservice.foodcourt.domain.validation.OrderUpdateRulesValidation;

import java.util.List;

public class OrderUseCase implements IOrderServicePort  {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IUserSessionPort userSessionPort;
    private final OrderUpdateRulesValidation orderUpdateRulesValidation;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, 
                        IRestaurantPersistencePort restaurantPersistencePort, 
                        IDishPersistencePort dishPersistencePort,
                        IUserSessionPort userSessionPort,
                        OrderUpdateRulesValidation orderUpdateRulesValidation) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.userSessionPort = userSessionPort;
        this.orderUpdateRulesValidation = orderUpdateRulesValidation;
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

    @Override
    public PageResult<OrderModel> getOrders(Integer page, Integer size, OrderStatusModel status) {
        if (page < DomainConstants.PAGE_MIN) throw new InvalidPaginationParameterException(DomainConstants.INVALID_PAGE);
        if (size < DomainConstants.SIZE_MIN) throw new InvalidPaginationParameterException(DomainConstants.INVALID_SIZE);
        Long employeeId = userSessionPort.getUserId();
        Long restaurantIdByEmployee = restaurantPersistencePort.getRestaurantByEmployee(employeeId);
        restaurantPersistencePort.validateExist(restaurantIdByEmployee);
        return orderPersistencePort.getOrders(page, size, restaurantIdByEmployee, status);
    }

    @Override
    public void startOrderPreparation(Long orderId) {
        Long chefId = userSessionPort.getUserId();
        boolean isAlreadyAssigned = orderPersistencePort.isOrderAlreadyAssignedToEmployee(chefId, orderId);
        OrderModel orderFound = orderPersistencePort.getOrderById(orderId);
        Long restaurantIdByEmployee = restaurantPersistencePort.getRestaurantByEmployee(chefId);

        orderUpdateRulesValidation.validateDataUpdate(
                isAlreadyAssigned,
                orderFound.getChefId(),
                restaurantIdByEmployee,
                orderFound.getRestaurant().getId());

        orderFound.setChefId(chefId);
        orderFound.setStatus(OrderStatusModel.PREPARACION);
        orderPersistencePort.updateOrder(orderFound);
    }

    @Override
    public void markOrderAsReady(Long orderId) {
        Long chefId = userSessionPort.getUserId();
        OrderModel orderFound = orderPersistencePort.getOrderById(orderId);
        Long restaurantIdByEmployee = restaurantPersistencePort.getRestaurantByEmployee(chefId);
        orderUpdateRulesValidation.validateDataMarkReady(
                restaurantIdByEmployee,
                orderFound.getRestaurant().getId(),
                orderFound.getChefId(),
                chefId,
                orderFound.getStatus()
        );
        String phoneNumberCustomer = orderPersistencePort.getPhoneNumberUser(orderFound.getCustomerId());
        String codeVerification = orderPersistencePort.getCodeVerification(phoneNumberCustomer);
        orderFound.setStatus(OrderStatusModel.LISTO);
        orderFound.setCodeVerification(codeVerification);
        orderPersistencePort.updateOrder(orderFound);
    }

    @Override
    public void markOrderAsDelivered(Long orderId, String codeProvideByCustomer) {
        Long chefId = userSessionPort.getUserId();
        OrderModel orderFound = orderPersistencePort.getOrderById(orderId);
        Long restaurantIdByEmployee = restaurantPersistencePort.getRestaurantByEmployee(chefId);
        orderUpdateRulesValidation.validateDataMarkDelivered(
                restaurantIdByEmployee,
                orderFound.getRestaurant().getId(),
                orderFound.getChefId(),
                chefId,
                orderFound.getStatus()
        );
        if (!codeProvideByCustomer.equals(orderFound.getCodeVerification())) {
            throw new InvalidVerificationCodeException();
        }
        orderFound.setStatus(OrderStatusModel.ENTREGADO);
        orderPersistencePort.updateOrder(orderFound);
    }

    @Override
    public void markOrderAsCancelled(Long orderId) {
        Long customerId = userSessionPort.getUserId();
        OrderModel orderFound = orderPersistencePort.getOrderById(orderId);
        orderUpdateRulesValidation.validateDataMarkCancelled(
                orderFound.getCustomerId(),
                customerId,
                orderFound.getStatus()
        );
        orderFound.setStatus(OrderStatusModel.CANCELADO);
        orderPersistencePort.updateOrder(orderFound);
    }


}
