package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.model.DishModel;
import com.microservice.foodcourt.domain.spi.IDishPersistencePort;
import com.microservice.foodcourt.infrastructure.dto.AuthInfo;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.DishEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
}
