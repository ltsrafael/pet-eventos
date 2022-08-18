package com.rafael.peteventos.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private boolean isActive;

    @Column
    private boolean isCancelled;

    @Column
    private String place;

    @ManyToMany
    private List<User> users;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Section> sections;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    private Long total;

}
