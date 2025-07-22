package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    @Query("SELECT d FROM DishEntity d WHERE d.restaurant.id = :restaurantId AND (:categoryId IS NULL OR d.category.id = :categoryId)")
    Page<DishEntity> findByRestaurantAndOptionalCategory(@Param("restaurantId") Long restaurantId, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT COUNT(d) FROM DishEntity d WHERE d.id IN :dishedId AND d.restaurant.id = :restaurantId ")
    long findValidDishIds(@Param("dishedId") List<Long> dishesId, @Param("restaurantId") Long restaurantId);


}
