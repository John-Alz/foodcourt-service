package com.microservice.foodcourt.application.handler;

import com.microservice.foodcourt.application.dto.request.DishChangeStatusDto;
import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.request.DishUpdateRequestDto;
import com.microservice.foodcourt.application.dto.response.DishResponseDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;
import com.microservice.foodcourt.domain.model.PageResult;

public interface IDishHandler {

    SaveMessageResponse saveDish(DishRequestDto dishRequestDto);
    SaveMessageResponse updateDish(Long id, DishUpdateRequestDto dishUpdateRequestDto);
    SaveMessageResponse changeDishStatus(Long id, DishChangeStatusDto dishChangeStatusDto);
    PageResult<DishResponseDto> getDishes(Integer page, Integer size, Long restaurantId, Long categoryId);

}
