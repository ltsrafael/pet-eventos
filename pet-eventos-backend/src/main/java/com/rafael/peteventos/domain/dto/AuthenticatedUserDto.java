package com.rafael.peteventos.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticatedUserDto {
    private String email;
    private String password;
}
