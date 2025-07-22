package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Page<RestaurantEntity> findAll(Pageable paging);

}
