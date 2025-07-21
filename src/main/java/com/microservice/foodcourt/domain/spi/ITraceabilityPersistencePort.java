package com.microservice.foodcourt.domain.spi;

import com.microservice.foodcourt.domain.dto.TraceabilityRequestDto;

public interface ITraceabilityPersistencePort {

    void saveOrderLog(TraceabilityRequestDto traceabilityRequestDto);

}
