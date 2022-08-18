package com.rafael.peteventos.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAttendance;

    @ManyToOne()
    private User user;

    @ManyToOne()
    private Event event;

    @ManyToOne()
    private Section section;
}
