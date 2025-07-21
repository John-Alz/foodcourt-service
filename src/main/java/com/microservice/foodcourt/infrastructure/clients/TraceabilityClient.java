package com.microservice.foodcourt.infrastructure.clients;

import com.microservice.foodcourt.domain.dto.TraceabilityRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "traceability", url = "http://localhost:8083/api/v1/orders/tracking")
public interface TraceabilityClient {

    @PostMapping()
    void createOrderLog(@RequestBody TraceabilityRequestDto traceabilityRequestDto);

}
