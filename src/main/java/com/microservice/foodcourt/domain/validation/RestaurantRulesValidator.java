package com.microservice.foodcourt.domain.validation;

import com.microservice.foodcourt.domain.exception.InvalidFieldException;
import com.microservice.foodcourt.domain.exception.RequiredFieldException;
import com.microservice.foodcourt.domain.model.RestaurantModel;
import com.microservice.foodcourt.domain.utils.DomainConstants;

import java.util.regex.Pattern;

public class RestaurantRulesValidator {

    private static final Pattern ONLY_NUMBERS_PATTERN = Pattern.compile(DomainConstants.REGEX_ONLY_NUMBER);
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(DomainConstants.REGEX_PHONE_NUMBER);
    private static final Pattern NAME_PATTERN = Pattern.compile(DomainConstants.REGEX_NAME);

    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_NAME);
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidFieldException(DomainConstants.INVALID_NAME);
        }
    }

    public void validateNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_NIT);
        }
        if (!ONLY_NUMBERS_PATTERN.matcher(nit).matches()) {
            throw new InvalidFieldException(DomainConstants.INVALID_NIT);
        }
    }

    public void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_ADDRESS);
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_PHONENUMBER);
        }
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidFieldException(DomainConstants.INVALID_PHONENUMBER);
        }
    }

    public void validateLogo(String urlLogo) {
        if (urlLogo == null || urlLogo.trim().isEmpty()) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_URLLOGO);
        }
    }

    public void validateOwnerId(Long ownerId) {
        if (ownerId == null) {
            throw new RequiredFieldException(DomainConstants.REQUIRED_OWNERID);
        }
    }

    public void validateRestaurantData(RestaurantModel restaurantModel) {
        validateName(restaurantModel.getName());
        validateNit(restaurantModel.getNit());
        validateAddress(restaurantModel.getAddress());
        validatePhoneNumber(restaurantModel.getPhoneNumber());
        validateLogo(restaurantModel.getUrlLogo());
        validateOwnerId(restaurantModel.getOwnerId());
    }

}
