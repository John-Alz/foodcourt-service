package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.dto.TraceabilityRequestDto;
import com.microservice.foodcourt.domain.dto.UserContactInfoDto;
import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.exception.InvalidVerificationCodeException;
import com.microservice.foodcourt.domain.exception.UnauthorizedActionException;
import com.microservice.foodcourt.domain.model.*;
import com.microservice.foodcourt.domain.spi.*;
import com.microservice.foodcourt.domain.validation.OrderUpdateRulesValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
    private ITraceabilityPersistencePort traceabilityPersistencePort;

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
        Long customerId = 99L;
        Long restaurantId = 1L;
        Long dishId1 = 10L;
        Long dishId2 = 20L;

        DishModel dish1 = new DishModel();
        dish1.setId(dishId1);
        DishModel dish2 = new DishModel();
        dish2.setId(dishId2);

        DishOrderModel dishOrder1 = new DishOrderModel();
        dishOrder1.setDish(dish1);
        dishOrder1.setAmount(1);

        DishOrderModel dishOrder2 = new DishOrderModel();
        dishOrder2.setDish(dish2);
        dishOrder2.setAmount(2);

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel orderModel = new OrderModel();
        orderModel.setRestaurant(restaurant);
        orderModel.setDishes(List.of(dishOrder1, dishOrder2));

        OrderModel orderSaved = new OrderModel();
        orderSaved.setId(500L);
        orderSaved.setStatus(OrderStatusModel.PENDIENTE);
        orderSaved.setCustomerId(customerId);
        orderSaved.setChefId(123L);
        orderSaved.setRestaurant(restaurant);

        when(userSessionPort.getUserId()).thenReturn(customerId);
        when(dishPersistencePort.findById(dishId1)).thenReturn(dish1);
        when(dishPersistencePort.findById(dishId2)).thenReturn(dish2);
        when(orderPersistencePort.saveOrder(orderModel)).thenReturn(orderSaved);
        when(orderPersistencePort.getInfoContactUser(customerId))
                .thenReturn(new UserContactInfoDto("+573001234567", "client@email.com"));
        when(userSessionPort.getUserEmail()).thenReturn("chef@email.com");

        // Act
        orderUseCase.saveOrder(orderModel);

        // Assert
        assertEquals(customerId, orderModel.getCustomerId());

        verify(userSessionPort).getUserId();
        verify(orderPersistencePort).existsOrderInProcessByCustomerId(eq(customerId), anyList());
        verify(restaurantPersistencePort).validateExist(restaurantId);
        verify(dishPersistencePort).findById(dishId1);
        verify(dishPersistencePort).findById(dishId2);
        verify(dishPersistencePort).validateAllDishesBelongToRestaurant(List.of(dishId1, dishId2), restaurantId);
        verify(orderPersistencePort).saveOrder(orderModel);
        verify(traceabilityPersistencePort).saveOrderLog(any(TraceabilityRequestDto.class));
    }


    @Test
    void saveOrder_ShouldValidateOrderStatusCheckBeforeSaving() {
        Long customerId = 42L;
        Long restaurantId = 99L;
        Long dishId = 1L;

        // Crear datos mínimos para que el método no lance excepción
        DishModel dishModel = new DishModel();
        dishModel.setId(dishId);

        DishOrderModel dishOrderModel = new DishOrderModel();
        dishOrderModel.setDish(dishModel);
        dishOrderModel.setAmount(2);

        RestaurantModel restaurantModel = new RestaurantModel();
        restaurantModel.setId(restaurantId);

        OrderModel orderModel = new OrderModel();
        orderModel.setRestaurant(restaurantModel);
        orderModel.setDishes(List.of(dishOrderModel));

        OrderModel savedOrder = new OrderModel();
        savedOrder.setId(123L);
        savedOrder.setStatus(OrderStatusModel.PENDIENTE);
        savedOrder.setCustomerId(customerId);
        savedOrder.setChefId(99L);
        savedOrder.setRestaurant(restaurantModel);

        when(userSessionPort.getUserId()).thenReturn(customerId);
        when(dishPersistencePort.findById(dishId)).thenReturn(dishModel);
        doNothing().when(restaurantPersistencePort).validateExist(restaurantId);
        doNothing().when(dishPersistencePort).validateAllDishesBelongToRestaurant(anyList(), eq(restaurantId));
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenReturn(savedOrder);
        when(orderPersistencePort.getInfoContactUser(anyLong()))
                .thenReturn(new UserContactInfoDto("+573000000000", "client@email.com"));
        when(userSessionPort.getUserEmail()).thenReturn("chef@email.com");

        // Act
        orderUseCase.saveOrder(orderModel);

        // Assert
        verify(orderPersistencePort, times(1)).existsOrderInProcessByCustomerId(eq(customerId), argThat(statusList ->
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
        Long customerId = 400L;
        String chefEmail = "chef@email.com";
        String customerEmail = "cliente@email.com";
        String customerPhone = "+573001234567";

        // Restaurante
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        // Orden
        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatusModel.PENDIENTE);
        order.setChefId(null);
        order.setCustomerId(customerId); // 👈 importante para trazabilidad

        // Stubs requeridos
        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(userSessionPort.getUserEmail()).thenReturn(chefEmail);
        when(orderPersistencePort.isOrderAlreadyAssignedToEmployee(chefId, orderId)).thenReturn(false);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);
        when(orderPersistencePort.getInfoContactUser(customerId))
                .thenReturn(new UserContactInfoDto(customerPhone, customerEmail));

        // Act
        orderUseCase.startOrderPreparation(orderId);

        // Assert
        assertEquals(chefId, order.getChefId());
        assertEquals(OrderStatusModel.PREPARACION, order.getStatus());

        verify(orderUpdateRulesValidation).validateDataUpdate(
                false, null, restaurantId, restaurantId
        );
        verify(orderPersistencePort).updateOrder(order);
        verify(traceabilityPersistencePort).saveOrderLog(any(TraceabilityRequestDto.class));
    }


    @Test
    void markOrderAsReady_ShouldUpdateOrderCorrectly() {
        // Datos de prueba
        Long chefId = 1L;
        Long restaurantId = 10L;
        Long customerId = 20L;
        Long orderId = 100L;
        String phone = "+573001234567";
        String email = "email@email.com";
        String code = "XYZ789";
        String chefEmail = "chef@email.com";

        // Mock de DTO
        UserContactInfoDto userContactInfoDto = new UserContactInfoDto(phone, email);

        // Restaurante y orden
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setChefId(chefId);
        order.setCustomerId(customerId);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatusModel.PREPARACION);

        // Stubbing
        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(userSessionPort.getUserEmail()).thenReturn(chefEmail);
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);
        when(orderPersistencePort.getInfoContactUser(customerId)).thenReturn(userContactInfoDto);
        when(orderPersistencePort.getCodeVerification(phone)).thenReturn(code);

        // Act
        orderUseCase.markOrderAsReady(orderId);

        // Assert
        assertEquals(OrderStatusModel.LISTO, order.getStatus());
        assertEquals(code, order.getCodeVerification());

        verify(orderUpdateRulesValidation).validateDataMarkReady(
                restaurantId, restaurantId, chefId, chefId, OrderStatusModel.PREPARACION
        );
        verify(orderPersistencePort).updateOrder(order);
        verify(traceabilityPersistencePort).saveOrderLog(any(TraceabilityRequestDto.class));
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
        Long customerId = 50L;

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(restaurantId);

        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setChefId(chefId);
        order.setCodeVerification(validCode);
        order.setStatus(OrderStatusModel.LISTO);
        order.setRestaurant(restaurant);
        order.setCustomerId(customerId);

        when(userSessionPort.getUserId()).thenReturn(chefId);
        when(userSessionPort.getUserEmail()).thenReturn("chef@email.com");
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(restaurantPersistencePort.getRestaurantByEmployee(chefId)).thenReturn(restaurantId);
        when(orderPersistencePort.getInfoContactUser(customerId))
                .thenReturn(new UserContactInfoDto("+573145678923", "cliente@email.com"));

        // Act
        orderUseCase.markOrderAsDelivered(orderId, validCode);

        // Assert
        assertEquals(OrderStatusModel.ENTREGADO, order.getStatus());

        verify(orderUpdateRulesValidation).validateDataMarkDelivered(
                restaurantId, restaurantId, chefId, chefId, OrderStatusModel.LISTO
        );
        verify(orderPersistencePort).updateOrder(order);
        verify(traceabilityPersistencePort).saveOrderLog(any(TraceabilityRequestDto.class));
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
        Long restaurantId = 100L;
        Long chefId = 10L;

        OrderModel order = new OrderModel();
        order.setId(orderId);
        order.setCustomerId(customerId);
        order.setChefId(chefId);
        order.setRestaurant(new RestaurantModel());
        order.getRestaurant().setId(restaurantId);
        order.setStatus(OrderStatusModel.PENDIENTE);

        when(userSessionPort.getUserId()).thenReturn(customerId);
        when(userSessionPort.getUserEmail()).thenReturn("chef@email.com");
        when(orderPersistencePort.getOrderById(orderId)).thenReturn(order);
        when(orderPersistencePort.getInfoContactUser(customerId))
                .thenReturn(new UserContactInfoDto("+573145678923", "cliente@email.com"));

        // Act
        orderUseCase.markOrderAsCancelled(orderId);

        // Assert
        assertEquals(OrderStatusModel.CANCELADO, order.getStatus());

        verify(orderUpdateRulesValidation).validateDataMarkCancelled(
                customerId, customerId, OrderStatusModel.PENDIENTE
        );

        verify(orderPersistencePort).updateOrder(order);
        verify(traceabilityPersistencePort).saveOrderLog(any(TraceabilityRequestDto.class));
    }



    @Test
    void recordTraceability() {
        // Arrange
        Long customerId = 5L;
        Long chefId = 10L;
        Long orderId = 100L;
        Long restaurantId = 50L;
        String customerEmail = "email@email.com";
        String chefEmail = "chef@email.com";

        OrderModel orderModel = new OrderModel();
        orderModel.setId(orderId);
        orderModel.setCustomerId(customerId);
        orderModel.setChefId(chefId);
        orderModel.setRestaurant(new RestaurantModel());
        orderModel.getRestaurant().setId(restaurantId);

        OrderStatusModel prevStatus = OrderStatusModel.PENDIENTE;
        OrderStatusModel newStatus = OrderStatusModel.PREPARACION;

        UserContactInfoDto userContactInfoCustomer = new UserContactInfoDto("+573145678923", customerEmail);

        when(userSessionPort.getUserEmail()).thenReturn(chefEmail);
        when(orderPersistencePort.getInfoContactUser(customerId)).thenReturn(userContactInfoCustomer);

        // Act
        orderUseCase.reecordOrderTraceability(orderModel, prevStatus, newStatus);

        // Assert
        TraceabilityRequestDto expectedDto = new TraceabilityRequestDto(
                orderId,
                customerId,
                customerEmail,
                prevStatus.toString(),
                newStatus.toString(),
                chefId,
                chefEmail,
                restaurantId
        );

        verify(traceabilityPersistencePort).saveOrderLog(expectedDto);
    }


}
