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
        DishEntity disFound = dishRepository.findById(id).orElse(null);
        if (disFound == null) {
            throw new NoDataFoundException("No existe el plato con ese id.");
        }
        return dishEntityMapper.toModel(disFound);
    }

    @Override
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthInfo authInfo = (AuthInfo) authentication.getPrincipal();
        return authInfo.id();
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
