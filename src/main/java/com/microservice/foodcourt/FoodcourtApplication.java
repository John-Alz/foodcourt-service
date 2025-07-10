package com.microservice.foodcourt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FoodcourtApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodcourtApplication.class, args);
	}

}
