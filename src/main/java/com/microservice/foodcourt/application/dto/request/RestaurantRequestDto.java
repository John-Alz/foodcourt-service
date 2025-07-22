package com.microservice.foodcourt.application.dto.request;

public record RestaurantRequestDto(
         String name,
         String nit,
         String address,
         String phoneNumber,
         String urlLogo,
         Long ownerId
) {
}
