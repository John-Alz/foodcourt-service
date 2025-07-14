package com.microservice.foodcourt.infrastructure.out.jpa.repository;

import com.microservice.foodcourt.domain.model.OrderStatusModel;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT COUNT(o) > 0 FROM OrderEntity o WHERE o.customerId = ?1 AND o.status IN ?2")
    boolean existsByCustomerIdAndStatuses(Long customerId, List<OrderStatusEntity> orderStatus);

}
