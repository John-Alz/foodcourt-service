package com.microservice.foodcourt.application.handler.impl;

import com.microservice.foodcourt.application.dto.request.DishChangeStatusDto;
import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.request.DishUpdateRequestDto;
import com.microservice.foodcourt.application.dto.response.DishResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.application.handler.IDishHandler;
import com.microservice.foodcourt.application.mapper.IDishMapper;
import com.microservice.foodcourt.application.utils.ApplicationConstants;
import com.microservice.foodcourt.domain.api.IDishServicePort;
import com.microservice.foodcourt.domain.model.PageResult;
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
        return new SaveMessageResponse(ApplicationConstants.CREATED_DISH_MESSAGE, LocalDateTime.now());
    }

    @Override
    public SaveMessageResponse updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto) {
        dishServicePort.updateDish(id, dishMapper.updateRequestToModel(dishUpdateRequestDto));
        return new SaveMessageResponse(ApplicationConstants.UPDATED_DISH_MESSAGE, LocalDateTime.now());
    }

    @Override
    public SaveMessageResponse changeDishStatus(Long id, DishChangeStatusDto dishChangeStatusDto) {
        dishServicePort.changeDishStatus(id, dishChangeStatusDto.active());
        return new SaveMessageResponse(ApplicationConstants.UPDATED_DISH_STATUS_MESSAGE, LocalDateTime.now());
    }

    @Override
    public PageResult<DishResponseDto> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId) {
        return dishMapper.modelListToResponseList(dishServicePort.getDishes(page, size, restaurantId, categoryId));
    }
}
