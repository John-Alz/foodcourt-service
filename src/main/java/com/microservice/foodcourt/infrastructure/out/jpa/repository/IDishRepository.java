package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
}
