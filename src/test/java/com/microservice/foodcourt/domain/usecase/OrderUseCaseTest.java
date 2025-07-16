package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.exception.InvalidVerificationCodeException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;
import com.microservice.foodcourt.domain.model.*;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IOrderPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.spi.IUserSessionPort;
import com.microservice.foodcourt.domain.validation.OrderUpdateRulesValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private OrderUpdateRulesValidation orderUpdateRulesValidation;

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

        when(userSessionPort.getUserId()).thenReturn(42L);
        when(dishPersistencePort.findById(anyLong())).thenReturn(new DishModel());


        orderUseCase.saveOrder(orderModel);


        verify(orderPersistencePort, times(1)).existsOrderInProcessByCustomerId(eq(42L), argThat(statusList ->
                statusList.contains(OrderStatusModel.PENDIENTE) &&
                        statusList.contains(OrderStatusModel.PREPARACION) &&
                        statusList.contains(OrderStatusModel.LISTO)
        ));
    }

    @Test
    void getOrders_shouldReturnListOfRestaurants() {
        OrderModel orderModel1 = new OrderModel();
        OrderModel orderModel2 = new OrderModel();
        List<OrderModel> ordersMock = Arrays.asList(
                orderModel1,
                orderModel2
        );

        PageResult pageResultMock = new PageResult<>(
                ordersMock,
                0,
                2,
                4,
                8L
        );

        when(orderPersistencePort.getOrders(0, 2, 10L, OrderStatusModel.PENDIENTE)).thenReturn(pageResultMock);
        when(userSessionPort.getUserId()).thenReturn(10L);
        when(restaurantPersistencePort.getRestaurantByEmployee(10L)).thenReturn(10L);

        PageResult<OrderModel> result = orderUseCase.getOrders(0, 2, OrderStatusModel.PENDIENTE);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(4, result.getTotalPages());
        assertEquals(8L, result.getTotalElements());
        verify(orderPersistencePort, times(1)).getOrders(0, 2, 10L, OrderStatusModel.PENDIENTE);
        verify(userSessionPort, times(1)).getUserId();
        verify(restaurantPersistencePort, times(1)).getRestaurantByEmployee(10L);
    }

    @Test
    void getOrders_shouldThrowException_WhenPageIsInvalid() {

        assertThrows(InvalidPaginationParameterException.class, () -> {
            orderUseCase.getOrders(-2, 2, OrderStatusModel.PENDIENTE);
        });

        verify(orderPersistencePort, never()).getOrders(anyInt(), anyInt(), anyLong(), any());

    }

    @Test
    void getOrders_shouldThrowException_WhenSizeIsInvalid() {

        assertThrows(InvalidPaginationParameterException.class, () -> {
            orderUseCase.getOrders(2, -2, OrderStatusModel.PENDIENTE);
        });

        verify(orderPersistencePort, never()).getOrders(anyInt(), anyInt(), anyLong(), any());

    }

    @Test
    void startOrderPreparation_ShouldUpdateOrder_WhenValidationPasses() {
        Long chefId = 100L;
        Long orderId = 200L;
        Long restaurantId = 300L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatusModel.PENDIENTE);
        order.setChefId(null);

        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(orderPersistencePort.isOrderAlreadyAssignedToEmployee(chefId, orderId)).thenReturn(false);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);

        // No exception expected
        orderUseCase.startOrderPreparation(orderId);

        assertEquals(chefId, order.getChefId());
        assertEquals(OrderStatusModel.PREPARACION, order.getStatus());

        verify(orderUpdateRulesValidation).validateDataUpdate(false, null, restaurantId, restaurantId);
        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void markOrderAsReady_ShouldUpdateOrderCorrectly() {
        Long chefId = 1L;
        Long restaurantId = 10L;
        Long customerId = 20L;
        Long orderId = 100L;
        String phone = "+573001234567";
        String code = "XYZ789";

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setChefId(chefId);
        order.setCustomerId(customerId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatusModel.PREPARACION);

        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);
        when(orderPersistencePort.getPhoneNumberUser(customerId)).thenReturn(phone);
        when(orderPersistencePort.getCodeVerification(phone)).thenReturn(code);

        orderUseCase.markOrderAsReady(orderId);

        assertEquals(OrderStatusModel.LISTO, order.getStatus());
        assertEquals(code, order.getCodeVerification());
        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void markOrderAsReady_ShouldThrow_WhenRestaurantMismatch() {
        Long chefId = 1L;
        Long restaurantIdFromOrder = 10L;
        Long restaurantIdByEmployee = 99L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantIdFromOrder);

        OrderModel order = new OrderModel();
        order.setChefId(chefId);
        order.setStatus(OrderStatusModel.PREPARACION);
        order.setCustomerId(50L);
        order.setRestaurant(restaurant);

        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(orderPersistencePort.getOrderById(anyLong())).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantIdByEmployee);

        doThrow(new UnauthorizedActionException("Restaurante no coincide"))
                .when(orderUpdateRulesValidation)
                .validateDataMarkReady(eq(restaurantIdByEmployee), eq(restaurantIdFromOrder), eq(chefId), eq(chefId), eq(OrderStatusModel.PREPARACION));

        assertThrows(UnauthorizedActionException.class, () -> orderUseCase.markOrderAsReady(123L));
    }

    @Test
    void markOrderAsReady_ShouldThrow_WhenChefMismatch() {
        Long restaurantId = 10L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setChefId(999L);
        order.setStatus(OrderStatusModel.PREPARACION);
        order.setCustomerId(50L);
        order.setRestaurant(restaurant);

        when(userSessionPort.getUserId()).thenReturn(1L);
        when(orderPersistencePort.getOrderById(anyLong())).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(1L)).thenReturn(restaurantId);

        doThrow(new UnauthorizedActionException("Chef no coincide"))
                .when(orderUpdateRulesValidation)
                .validateDataMarkReady(eq(restaurantId), eq(restaurantId), eq(999L), eq(1L), eq(OrderStatusModel.PREPARACION));

        assertThrows(UnauthorizedActionException.class, () -> orderUseCase.markOrderAsReady(123L));
    }

    @Test
    void markOrderDelivered_ShouldThrow_WhenChefMismatch() {
        Long restaurantId = 10L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setChefId(999L); // No coincide con el ID autenticado
        order.setStatus(OrderStatusModel.LISTO);
        order.setCodeVerification("abc123");
        order.setRestaurant(restaurant);

        when(userSessionPort.getUserId()).thenReturn(1L);
        when(orderPersistencePort.getOrderById(anyLong())).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(1L)).thenReturn(restaurantId);

        doThrow(new UnauthorizedActionException("Chef no autorizado"))
                .when(orderUpdateRulesValidation)
                .validateDataMarkDelivered(eq(restaurantId), eq(restaurantId), eq(999L), eq(1L), eq(OrderStatusModel.LISTO));

        assertThrows(UnauthorizedActionException.class, () -> orderUseCase.markOrderAsDelivered(123L, "abc123"));
    }

    @Test
    void markOrderAsDelivered_ShouldUpdateOrder_WhenCodeIsValid() {
        Long chefId = 1L;
        Long orderId = 10L;
        String validCode = "abc123";
        Long restaurantId = 10L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setChefId(chefId);
        order.setCodeVerification(validCode);
        order.setStatus(OrderStatusModel.LISTO);
        order.setRestaurant(restaurant);

        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);

        orderUseCase.markOrderAsDelivered(orderId, validCode);

        assertEquals(OrderStatusModel.ENTREGADO, order.getStatus());
        verify(orderUpdateRulesValidation).validateDataMarkDelivered(
                restaurantId, restaurantId, chefId, chefId, OrderStatusModel.LISTO
        );
        verify(orderPersistencePort).updateOrder(order);
    }

    @Test
    void markOrderAsDelivered_ShouldThrow_WhenCodeIsInvalid() {
        Long chefId = 1L;
        Long orderId = 10L;
        String correctCode = "abc123";
        String wrongCode = "wrong456";
        Long restaurantId = 10L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setChefId(chefId);
        order.setCodeVerification(correctCode);
        order.setStatus(OrderStatusModel.LISTO);
        order.setRestaurant(restaurant);

        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);

        assertThrows(InvalidVerificationCodeException.class, () ->
                orderUseCase.markOrderAsDelivered(orderId, wrongCode));

        verify(orderUpdateRulesValidation).validateDataMarkDelivered(
                restaurantId, restaurantId, chefId, chefId, OrderStatusModel.LISTO
        );
        verify(orderPersistencePort, never()).updateOrder(any());
    }


    @Test
    void markOrderAsCancelled_ShouldUpdateOrder_WhenValid() {
        Long customerId = 5L;
        Long orderId = 20L;

        OrderModel order = new OrderModel();
        order.setCustomerId(customerId);
        order.setStatus(OrderStatusModel.PENDIENTE);

        when(userSessionPort.getUserId()).thenReturn(customerId);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);

        orderUseCase.markOrderAsCancelled(orderId);

        assertEquals(OrderStatusModel.CANCELADO, order.getStatus());
        verify(orderUpdateRulesValidation).validateDataMarkCancelled(
                customerId, customerId, OrderStatusModel.PENDIENTE
        );
        verify(orderPersistencePort).updateOrder(order);
    }



}
