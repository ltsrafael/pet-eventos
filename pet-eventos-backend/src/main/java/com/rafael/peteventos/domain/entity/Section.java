package com.rafael.peteventos.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSection;

    @Column
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Attendance> attendances;

}
