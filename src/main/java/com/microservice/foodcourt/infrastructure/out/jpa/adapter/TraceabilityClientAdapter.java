package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.dto.TraceabilityRequestDto;
import com.microservice.foodcourt.domain.spi.ITraceabilityPersistencePort;
import com.microservice.foodcourt.infrastructure.clients.TraceabilityClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TraceabilityClientAdapter implements ITraceabilityPersistencePort  {

    private final TraceabilityClient traceabilityClient;

    @Override
    public void saveOrderLog(TraceabilityRequestDto traceabilityRequestDto) {
        System.out.println(traceabilityRequestDto.orderId());
        traceabilityClient.createOrderLog(traceabilityRequestDto);
    }
}
