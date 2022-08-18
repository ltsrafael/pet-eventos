package com.rafael.peteventos.domain.dto;

import lombok.*;

import java.util.List;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private Long idGroup;
    private String name;
    private String description;
    private String course;
    private String center;
    private List<UserDto> users;
}
