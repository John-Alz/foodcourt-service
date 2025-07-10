package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.spi.ICategoryPersistencePort;
import com.microservice.foodcourt.infrastructure.exception.NoDataFoundException;
import com.microservice.foodcourt.infrastructure.out.jpa.entity.CategoryEntity;
import com.microservice.foodcourt.infrastructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;

    @Override
    public void existCategory(Long id) {
        CategoryEntity categoryFound = categoryRepository.findById(id).orElse(null);
        if (categoryFound == null) {
            throw new NoDataFoundException("Categoria no econtrada.");
        }
    }
}
