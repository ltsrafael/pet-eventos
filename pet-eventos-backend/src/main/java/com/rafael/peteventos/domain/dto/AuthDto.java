package com.rafael.peteventos.domain.dto;

import lombok.*;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private String token;
    private String userType;
}
