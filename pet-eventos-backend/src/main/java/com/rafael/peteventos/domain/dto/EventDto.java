package com.rafael.peteventos.domain.dto;

import lombok.*;

import java.util.List;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long idEvent;
    private String name;
    private String place;
    private String description;
    private boolean isActive;
    private boolean isCancelled;
    private List<UserDto> users;
    private List<SectionDto> sections;
    private List<AttendanceDto> attendances;
    private GroupDto group;
}
