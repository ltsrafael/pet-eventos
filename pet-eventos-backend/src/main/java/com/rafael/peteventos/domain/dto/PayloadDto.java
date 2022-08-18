package com.rafael.peteventos.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayloadDto {
    private String sub;
    private String exp;
    private String iat;
}
