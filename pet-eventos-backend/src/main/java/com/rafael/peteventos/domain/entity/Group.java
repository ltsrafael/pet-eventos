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
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGroup;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String center;

    @Column
    private String course;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<AuthenticatedUser> users;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Event> events;

}
