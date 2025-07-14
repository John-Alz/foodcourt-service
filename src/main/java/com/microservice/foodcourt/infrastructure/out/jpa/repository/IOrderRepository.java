package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderStatusEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT COUNT(o) > 0 FROM OrderEntity o WHERE o.customerId = ?1 AND o.status IN ?2")
    boolean existsByCustomerIdAndStatuses(Long customerId, List<OrderStatusEntity> orderStatus);

    @Query("SELECT o FROM OrderEntity o WHERE o.restaurant.id = :restaurantId AND (:status IS NULL OR o.status = :status)")
    Page<OrderEntity> findAllByRestaurant(@Param("restaurantId") Long restaurantId, @Param("status") OrderStatusEntity status, Pageable paging);


    @Query("SELECT COUNT(o) > 0 FROM OrderEntity o WHERE o.id = ?2 AND o.chefId = ?1")
    boolean existsByIdAndChefId(Long chefId, Long orderId);
}
