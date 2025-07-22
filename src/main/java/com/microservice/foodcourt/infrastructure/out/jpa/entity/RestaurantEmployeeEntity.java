package com.microservice.foodcourt.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_employees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantEmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación directa con el restaurante dentro del mismo microservicio
    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    // ID del usuario del microservicio de usuarios.
    // Es solo un número, NO una relación JPA.
    @Column(name = "employee_user_id", nullable = false)
    private Long employeeUserId;

}


