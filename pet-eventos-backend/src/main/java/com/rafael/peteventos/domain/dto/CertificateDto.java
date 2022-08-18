package com.rafael.peteventos.domain.dto;

import lombok.*;

@Builder(setterPrefix = "with")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDto {
    private String center;
    private String course;
    private String participantName;
    private String eventName;
    private String dates;
    private String hours;
    private String number;
}
