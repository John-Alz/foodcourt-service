package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.infrastructure.out.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
