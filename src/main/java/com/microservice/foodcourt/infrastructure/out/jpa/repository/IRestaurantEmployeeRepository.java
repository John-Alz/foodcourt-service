package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IRestaurantEmployeeRepository extends JpaRepository<RestaurantEmployeeEntity, Long> {

    @Query("SELECT e FROM RestaurantEmployeeEntity e WHERE e.employeeUserId = ?1")
    Optional<RestaurantEmployeeEntity> findRestaurantEmployeeEntityByEmployeeUserId(Long employeeId);

}
