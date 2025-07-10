package com.microservice.foodcourt.application.handler;

import com.microservice.foodcourt.application.dto.request.DishRequestDto;
import com.microservice.foodcourt.application.dto.response.SaveMessageResponse;

public interface IDishHandler {

    SaveMessageResponse saveDish(DishRequestDto dishRequestDto);

}
