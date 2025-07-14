package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.model.PageResult;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.infrastructure.dto.AuthInfo;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;

import java.util.List;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public void saveDish(DishModel dishModel) {
        dishRepository.save(dishEntityMapper.toEntity(dishModel));
    }


    @Override
    public DishModel findById(Long id) {
        DishEntity dishFound = dishRepository.findById(id).orElse(null);
        if (dishFound == null) {
            throw new NoDataFoundException("No existe el plato con ese id.");
        }
        return dishEntityMapper.toModel(dishFound);
    }

    @Override
    public void validateAllDishesBelongToRestaurant(List<Long> dishesId, Long restaurantId) {
        long count = dishRepository.findValidDishIds(dishesId, restaurantId);
        if (count != dishesId.size()) {
            throw new NoDataFoundException("Uno o más platos no pertenecen al restaurante.");
        }
    }


    @Override
    public PageResult<DishModel> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId) {

        Pageable paging = PageRequest.of(page, size);
        Page<DishEntity> dishPage = dishRepository.findByRestaurantAndOptionalCategory(restaurantId, categoryId, paging);
        List<DishModel> dishList = dishEntityMapper.entityListToModelList(dishPage.getContent());
        return new PageResult<>(
                dishList,
                dishPage.getNumber(),
                dishPage.getSize(),
                dishPage.getTotalPages(),
                dishPage.getTotalElements()
        );
    }
}
