package com.microservice.foodcourt.application.handler;

import com.microservice.foodcourt.application.dto.request.OrderRequestDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;

public interface IOrderHandler {

    SaveMessageResponse saveOrder(OrderRequestDto orderRequestDto);

}
