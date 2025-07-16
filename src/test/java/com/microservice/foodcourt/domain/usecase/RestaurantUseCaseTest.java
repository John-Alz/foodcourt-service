package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.exception.InvalidPaginationParameterException;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.validation.RestaurantRulesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantUseCaseTest {

    private IRestaurantPersistencePort restaurantPersistencePort;
    private RestaurantRulesValidator restaurantRulesValidator;
    private RestaurantUseCase restaurantUseCase;

    @BeforeEach
    void setUp() {
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        restaurantRulesValidator = mock(RestaurantRulesValidator.class);
        restaurantUseCase = new RestaurantUseCase(restaurantPersistencePort, restaurantRulesValidator);
    }

    @Test
    void saveRestaurant_ShouldValidateAndSave() {

        RestaurantModel restaurantModel = new RestaurantModel();

        restaurantUseCase.saveRestaurant(restaurantModel);

        verify(restaurantRulesValidator, times(1)).validateRestaurantData(restaurantModel);
        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurantModel);
        verifyNoMoreInteractions(restaurantRulesValidator, restaurantPersistencePort);
    }

    @Test
    void validateRestaurantOwnership() {
        Long restaurantId = 2L;
        Long userId = 12L;

        restaurantUseCase.validateRestaurantOwnership(restaurantId, userId);

        verify(restaurantPersistencePort, times(1)).validateRestaurantOwnership(restaurantId, userId);
    }

    @Test
    void getRestaurants_shouldReturnListOfRestaurants() {
        RestaurantModel restaurantOne = new RestaurantModel();
        RestaurantModel restaurantTwo = new RestaurantModel();
        List<RestaurantModel> restaurantMock = Arrays.asList(
                restaurantOne,
                restaurantTwo
        );

        PageResult pageResultMock = new PageResult<>(
                restaurantMock,
                0,
                2,
                4,
                8L
        );

        when(restaurantPersistencePort.getRestaurants(0, 2)).thenReturn(pageResultMock);

        PageResult<RestaurantModel> result = restaurantUseCase.getRestaurants(0, 2);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(4, result.getTotalPages());
        assertEquals(8L, result.getTotalElements());
        verify(restaurantPersistencePort, times(1)).getRestaurants(0, 2);
    }

    @Test
    void getRestaurants_shouldThrowException_WhenPageIsInvalid() {

        assertThrows(InvalidPaginationParameterException.class, () -> {
            restaurantUseCase.getRestaurants(-2, 20);
        });

        verify(restaurantPersistencePort, never()).getRestaurants(anyInt(), anyInt());

    }

    @Test
    void getRestaurants_shouldThrowException_WhenSizeIsInvalid() {

        assertThrows(InvalidPaginationParameterException.class, () -> {
            restaurantUseCase.getRestaurants(2, -20);
        });

        verify(restaurantPersistencePort, never()).getRestaurants(anyInt(), anyInt());

    }

    @Test
    void createEmployee_ShouldAddEmployeeWithRestaurant() {

        Long userId = 10L;
        Long restaurantId = 20L;

        restaurantUseCase.createEmployee(userId, restaurantId);

        verify(restaurantPersistencePort, times(1)).validateExist(restaurantId);
        verify(restaurantPersistencePort, times(1)).createEmployee(userId, restaurantId);
    }
}
