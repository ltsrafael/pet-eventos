package com.rafael.peteventos.domain.dto;

import lombok.*;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long idUser;
    private String name;
    private Integer registration;
    private String email;
    private String userType;
}
