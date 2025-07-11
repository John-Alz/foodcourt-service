package com.microservice.foodcourt.domain.usecase;

import com.microservice.foodcourt.domain.model.CategoryModel;
import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.ICategoryPersistencePort;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.domain.validation.DishRulesValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private DishRulesValidation dishRulesValidation;

    @InjectMocks
    private DishUseCase dishUseCase;

    private DishModel dishModel;
    private CategoryModel categoryModel;
    private RestaurantModel restaurantModel;

    @BeforeEach
    void setUp() {
        categoryModel = new CategoryModel();
        categoryModel.setId(1L);

        restaurantModel = new RestaurantModel();
        restaurantModel.setId(10L);

        dishModel = new DishModel();
        dishModel.setId(100L);
        dishModel.setName("Pizza Napolitana");
        dishModel.setCategory(categoryModel);
        dishModel.setDescription("Delicious Italian pizza");
        dishModel.setPrice(new BigDecimal("45000"));
        dishModel.setRestaurant(restaurantModel);
        dishModel.setImageUrl("https://image.com/pizza.jpg");
        dishModel.setActive(true);
    }

    @Test
    void saveDish_ShouldValidateAndSave() {
        dishUseCase.saveDish(dishModel);

        verify(dishRulesValidation, times(1)).validateDishData(dishModel);
        verify(categoryPersistencePort, times(1)).existCategory(1L);
        verify(restaurantPersistencePort, times(1)).validateExist(10L);
        verify(dishPersistencePort, times(1)).saveDish(dishModel);
    }

    @Test
    void updateDish_ShouldUpdateAndSaveDish() {
        // Arrange
        DishModel existingDish = new DishModel();
        existingDish.setId(100L);
        existingDish.setPrice(new BigDecimal("20000"));
        existingDish.setDescription("Old description");

        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(10L);
        existingDish.setRestaurant(restaurant);

        DishModel updateDish = new DishModel();
        updateDish.setPrice(new BigDecimal("25000"));
        updateDish.setDescription("New updated description");

        when(dishPersistencePort.findById(100L)).thenReturn(existingDish);
        when(dishPersistencePort.getUserId()).thenReturn(1L); // ✅ Esto faltaba

        // Act
        dishUseCase.updateDish(100L, updateDish);

        // Assert
        verify(dishPersistencePort).findById(100L);
        verify(dishPersistencePort).getUserId(); // ✅ ahora esto también ocurre
        verify(restaurantPersistencePort).validateRestaurantOwnership(10L, 1L);
        verify(dishPersistencePort).saveDish(existingDish);

        assert existingDish.getPrice().equals(new BigDecimal("25000"));
        assert existingDish.getDescription().equals("New updated description");
    }


    @Test
    void saveDish_ShouldValidateAndCheckOwnershipBeforeSave() {
        // Arrange
        when(dishPersistencePort.getUserId()).thenReturn(99L);

        // Act
        dishUseCase.saveDish(dishModel);

        // Assert
        verify(dishRulesValidation, times(1)).validateDishData(dishModel);
        verify(categoryPersistencePort, times(1)).existCategory(1L);
        verify(restaurantPersistencePort, times(1)).validateExist(10L);
        verify(dishPersistencePort, times(1)).getUserId();
        verify(restaurantPersistencePort, times(1)).validateRestaurantOwnership(10L, 99L);
        verify(dishPersistencePort, times(1)).saveDish(dishModel);
    }

    @Test
    void updateDish_ShouldThrowException_WhenDishNotFound() {
        // Arrange
        when(dishPersistencePort.findById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> dishUseCase.updateDish(999L, dishModel));

        verify(dishPersistencePort, times(1)).findById(999L);
        // El resto no se debería ejecutar
        verify(restaurantPersistencePort, never()).validateRestaurantOwnership(any(), any());
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void updateDish_ShouldValidateOwnershipWithFetchedUserId() {
        // Arrange
        DishModel existingDish = new DishModel();
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setId(10L);
        existingDish.setRestaurant(restaurant);

        existingDish.setPrice(new BigDecimal("10000"));
        existingDish.setDescription("Old");

        when(dishPersistencePort.findById(100L)).thenReturn(existingDish);
        when(dishPersistencePort.getUserId()).thenReturn(77L);

        DishModel updated = new DishModel();
        updated.setPrice(new BigDecimal("30000"));
        updated.setDescription("Updated desc");

        // Act
        dishUseCase.updateDish(100L, updated);

        // Assert
        verify(dishPersistencePort).findById(100L);
        verify(dishPersistencePort).getUserId();
        verify(restaurantPersistencePort).validateRestaurantOwnership(10L, 77L);
        verify(dishPersistencePort).saveDish(existingDish);

        assert existingDish.getPrice().equals(updated.getPrice());
        assert existingDish.getDescription().equals(updated.getDescription());
    }


}
