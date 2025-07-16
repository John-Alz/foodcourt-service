package com.microservice.foodcourt.infrastructure.clients;

import com.microservice.foodcourt.infrastructure.dto.CodeVerificationResponseDto;
import com.microservice.foodcourt.infrastructure.dto.PhoneNumberRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messaging", url = "http://localhost:8082/api/v1/send-code")
public interface MessagingClient {

    @PostMapping()
    public CodeVerificationResponseDto sendCodeVerification(@RequestBody PhoneNumberRequestDto phoneNumberRequestDto);


    }
