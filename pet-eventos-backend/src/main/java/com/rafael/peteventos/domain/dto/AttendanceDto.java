package com.rafael.peteventos.domain.dto;

import lombok.*;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private UserDto user;
    private EventDto event;
    private SectionDto section;
}
