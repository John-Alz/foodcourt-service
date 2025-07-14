package com.microservice.foodcourt.infrastructure.clients;

import com.microservice.foodcourt.infrastructure.dto.RestaurantIdResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users", url = "http://localhost:8080/api/v1/users")
public interface UserClient {

    @GetMapping("/{userId}")
    void validateUser(@PathVariable Long userId, @RequestParam String role);

    @GetMapping("/{userId}/restaurant")
    public RestaurantIdResponseDto getRestaurantByEmployee(@PathVariable Long userId);

}
