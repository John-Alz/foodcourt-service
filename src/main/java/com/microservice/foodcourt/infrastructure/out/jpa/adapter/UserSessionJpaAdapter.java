package com.microservice.foodcourt.infrastructure.out.jpa.adapter;

import com.microservice.foodcourt.domain.spi.IUserSessionPort;
import com.microservice.foodcourt.infrastructure.dto.AuthInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserSessionJpaAdapter implements IUserSessionPort {
    @Override
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthInfo authInfo = (AuthInfo) authentication.getPrincipal();
        return authInfo.id();
    }

    @Override
    public String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthInfo authInfo = (AuthInfo) authentication.getPrincipal();
        return authInfo.email();
    }
}
