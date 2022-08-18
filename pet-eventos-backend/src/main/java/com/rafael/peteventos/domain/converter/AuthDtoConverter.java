package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.AuthDto;

public class AuthDtoConverter {

    public static AuthDto convertAuth(String token, String userType) {
        return AuthDto.builder()
                .withToken(token)
                .withUserType(userType)
                .build();
    }
}
