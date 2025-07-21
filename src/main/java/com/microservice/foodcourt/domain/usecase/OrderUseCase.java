package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.api.IOrderServicePort;
import com.microservice.foodcourt.domain.dto.TraceabilityRequestDto;
import com.microservice.foodcourt.domain.dto.UserContactInfoDto;
import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.exception.InvalidVerificationCodeException;
import com.microservice.foodcourt.domain.model.OrderModel;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.spi.*;
import com.microservice.foodcourt.domain.utils.DomainConstants;
import com.microservice.foodcourt.domain.validation.OrderUpdateRulesValidation;

import java.util.List;

public class OrderUseCase implements IOrderServicePort  {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IUserSessionPort userSessionPort;
    private final OrderUpdateRulesValidation orderUpdateRulesValidation;
    private final ITraceabilityPersistencePort traceabilityPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IUserSessionPort userSessionPort,
                        OrderUpdateRulesValidation orderUpdateRulesValidation,
                        ITraceabilityPersistencePort traceabilityPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.userSessionPort = userSessionPort;
        this.orderUpdateRulesValidation = orderUpdateRulesValidation;
        this.traceabilityPersistencePort = traceabilityPersistencePort;
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
        OrderModel orderSaved = orderPersistencePort.saveOrder(orderModel);
        reecordOrderTraceability(orderSaved, orderSaved.getStatus(), orderSaved.getStatus());
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

        OrderStatusModel prevStatus = orderFound.getStatus();
        orderFound.setChefId(chefId);
        orderFound.setStatus(OrderStatusModel.PREPARACION);

        //Trazabilidad
        reecordOrderTraceability(orderFound, prevStatus, orderFound.getStatus());
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
        UserContactInfoDto userContactInfo = orderPersistencePort.getInfoContactUser(orderFound.getCustomerId());
        String codeVerification = orderPersistencePort.getCodeVerification(userContactInfo.phoneNumber());
        OrderStatusModel prevStatus = orderFound.getStatus();
        orderFound.setStatus(OrderStatusModel.LISTO);
        orderFound.setCodeVerification(codeVerification);

        //Trazabilidad
        reecordOrderTraceability(orderFound, prevStatus, orderFound.getStatus());
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
        OrderStatusModel prevStatus = orderFound.getStatus();
        orderFound.setStatus(OrderStatusModel.ENTREGADO);
        reecordOrderTraceability(orderFound, prevStatus, orderFound.getStatus());
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
        OrderStatusModel prevStatus = orderFound.getStatus();
        orderFound.setStatus(OrderStatusModel.CANCELADO);
        reecordOrderTraceability(orderFound, prevStatus, orderFound.getStatus());
        orderPersistencePort.updateOrder(orderFound);
    }


    //Gestionar trazabiliad
    public void reecordOrderTraceability(OrderModel order, OrderStatusModel prevStatus, OrderStatusModel newStatus) {
        String emailChef = userSessionPort.getUserEmail();

        UserContactInfoDto userContactInfoCustomer = orderPersistencePort.getInfoContactUser(order.getCustomerId());
        //Envio data a trazabilidad
        TraceabilityRequestDto traceabilityRequestDto = new TraceabilityRequestDto(
                order.getId(),
                order.getCustomerId(),
                userContactInfoCustomer.email(),
                prevStatus.toString(),
                newStatus.toString(),
                order.getChefId(),
                emailChef,
                order.getRestaurant().getId()
        );

        traceabilityPersistencePort.saveOrderLog(traceabilityRequestDto);
    }

}
