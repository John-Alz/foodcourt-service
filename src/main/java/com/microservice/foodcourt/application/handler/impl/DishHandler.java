package com.microservice.foodcourt.application.handler.impl;

import com.microservice.foodcourt.application.dto.request.DishChangeStatusDto;
import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.request.DishUpdateRequestDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IDishHandler;
import com.microservice.foodcourt.application.mapper.IDishMapper;
import com.microservice.foodcourt.domain.api.IDishServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishMapper dishMapper;


    @Override
    public SaveMessageResponse saveDish(DishRequestDto dishRequestDto) {
        dishServicePort.saveDish(dishMapper.requestToModel(dishRequestDto));
        return new SaveMessageResponse("Plato creado.", LocalDateTime.now());
    }

    @Override
    public SaveMessageResponse updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto) {
        dishServicePort.updateDish(id, dishMapper.updateRequestToModel(dishUpdateRequestDto));
        return new SaveMessageResponse("Plato actualizado", LocalDateTime.now());
    }

    @Override
    public SaveMessageResponse changeDishStatus(Long id, DishChangeStatusDto dishChangeStatusDto) {
        dishServicePort.changeDishStatus(id, dishChangeStatusDto.active());
        return new SaveMessageResponse("El estado del plato fue modificado.", LocalDateTime.now());
    }
}
