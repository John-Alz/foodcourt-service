package com.microservice.foodcourt.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dish_order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DishOrderEntity {

    @EmbeddedId
    private DishOrderEntityId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private DishEntity dish;

    private Integer amount;

}
