package com.microservice.foodcourt.application.handler;

import com.microservice.foodcourt.application.dto.request.OrderRequestDto;
import com.microservice.foodcourt.application.dto.response.OrderResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;

public interface IOrderHandler {

    SaveMessageResponse saveOrder(OrderRequestDto orderRequestDto);
    PageResult<OrderResponseDto> getOrders(Integer page, Integer size, OrderStatusModel status);

}
