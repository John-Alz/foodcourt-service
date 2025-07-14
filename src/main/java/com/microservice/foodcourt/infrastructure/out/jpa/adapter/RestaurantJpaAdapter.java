package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.spi.IRestaurantPersistencePort;
import com.microservice.foodcourt.infrastructure.clients.UserClient;
import com.microservice.foodcourt.infrastructure.dto.RestaurantIdResponseDto;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.exception.UnauthorizedException;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.RestaurantEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final UserClient userClient;

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        userClient.validateUser(restaurantModel.getOwnerId(), "OWNER");
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurantModel));
    }

    @Override
    public void validateExist(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new NoDataFoundException("No existe un restaurante con ese id");
        }
    }

    @Override
    public void validateRestaurantOwnership(Long restaurantId, Long userId) {
        RestaurantEntity restaurantFound = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurantFound == null) {
            throw new NoDataFoundException("Restaurante no encontrado.");
        }
        if (!restaurantFound.getOwnerId().equals(userId)) {
            throw new UnauthorizedException();
        }
    }

    @Override
    public PageResult<RestaurantModel> getRestaurants(Integer page, Integer size) {
        Sort sort = Sort.by("name").ascending();
        Pageable paging = PageRequest.of(page, size, sort);
        Page<RestaurantEntity> pageRestaurant = restaurantRepository.findAll(paging);
        List<RestaurantModel> restaurantList = restaurantEntityMapper.listEntityToListModel(pageRestaurant.getContent());
        return new PageResult<>(
                restaurantList,
                pageRestaurant.getNumber(),
                pageRestaurant.getSize(),
                pageRestaurant.getTotalPages(),
                pageRestaurant.getTotalElements()
        );
    }

    @Override
    public Long getRestaurantByEmployee(Long employeeId) {
        RestaurantIdResponseDto restaurantIdResponseDto = userClient.getRestaurantByEmployee(employeeId);
        return restaurantIdResponseDto.restaurantId();
    }

}
