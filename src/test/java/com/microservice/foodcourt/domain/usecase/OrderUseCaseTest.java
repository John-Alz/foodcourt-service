package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.model.*;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.spi.IUserSessionPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IUserSessionPort userSessionPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel orderModel;

    @BeforeEach
    void setUp() {
        // Simular platos
        DishModel dish1 = new DishModel();
        dish1.setId(10L);
        dish1.setName("Pizza");
        dish1.setPrice(BigDecimal.valueOf(50000));

        DishModel dish2 = new DishModel();
        dish2.setId(20L);
        dish2.setName("Pasta");
        dish2.setPrice(BigDecimal.valueOf(30000));

        DishOrderModel dishOrder1 = new DishOrderModel();
        dishOrder1.setDish(dish1);
        dishOrder1.setAmount(2);

        DishOrderModel dishOrder2 = new DishOrderModel();
        dishOrder2.setDish(dish2);
        dishOrder2.setAmount(1);

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(1L);
        restaurant.setName("Italiano");

        orderModel = new OrderModel();
        orderModel.setRestaurant(restaurant);
        orderModel.setDishes(List.of(dishOrder1, dishOrder2));
    }

    @Test
    void saveOrder_ShouldSetCustomerIdAndCallDependencies() {
        // Arrange
        when(userSessionPort.getUserId()).thenReturn(99L);
        when(dishPersistencePort.findById(10L)).thenReturn(new DishModel());
        when(dishPersistencePort.findById(20L)).thenReturn(new DishModel());

        // Act
        orderUseCase.saveOrder(orderModel);

        // Assert
        verify(userSessionPort).getUserId();
        verify(orderPersistencePort).existsOrderInProcessByCustomerId(eq(99L), anyList());
        verify(restaurantPersistencePort).validateExist(1L);
        verify(dishPersistencePort).findById(10L);
        verify(dishPersistencePort).findById(20L);
        verify(dishPersistencePort).validateAllDishesBelongToRestaurant(List.of(10L, 20L), 1L);
        verify(orderPersistencePort).saveOrder(orderModel);

        assertEquals(99L, orderModel.getCustomerId());
    }

    @Test
    void saveOrder_ShouldValidateOrderStatusCheckBeforeSaving() {
        // Arrange
        when(userSessionPort.getUserId()).thenReturn(42L);
        when(dishPersistencePort.findById(anyLong())).thenReturn(new DishModel());

        // Act
        orderUseCase.saveOrder(orderModel);

        // Assert
        verify(orderPersistencePort, times(1)).existsOrderInProcessByCustomerId(eq(42L), argThat(statusList ->
                statusList.contains(OrderStatusModel.PENDIENTE) &&
                        statusList.contains(OrderStatusModel.PREPARACION) &&
                        statusList.contains(OrderStatusModel.LISTO)
        ));
    }
}
