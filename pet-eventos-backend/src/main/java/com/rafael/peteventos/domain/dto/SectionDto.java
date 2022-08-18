package com.rafael.peteventos.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto {
    private Long idSection;
    private String name;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
}
