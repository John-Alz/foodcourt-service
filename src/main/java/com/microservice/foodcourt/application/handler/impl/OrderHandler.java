package com.microservice.foodcourt.application.handler.impl;

import com.microservice.foodcourt.application.dto.request.OrderRequestDto;
import com.microservice.foodcourt.application.dto.response.OrderResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IOrderHandler;
import com.microservice.foodcourt.application.mapper.IOrderMapper;
import com.microservice.foodcourt.domain.api.IOrderServicePort;
import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.domain.model.PageResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderMapper orderMapper;

    @Override
    public SaveMessageResponse saveOrder(OrderRequestDto orderRequestDto) {
        orderServicePort.saveOrder(orderMapper.requestToModel(orderRequestDto));
        return new SaveMessageResponse("Pedido creado.", LocalDateTime.now());
    }

    @Override
    public PageResult<OrderResponseDto> getOrders(Integer page, Integer size, OrderStatusModel status) {
        return orderMapper.modelListToResponseList(orderServicePort.getOrders(page, size, status));
    }

    @Override
    public SaveMessageResponse startOrderPreparation(Long orderId) {
        orderServicePort.startOrderPreparation(orderId);
        return new SaveMessageResponse("El pedido esta en preparacion", LocalDateTime.now());
    }
}
