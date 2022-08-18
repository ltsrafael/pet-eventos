package com.rafael.peteventos.integration.repository.impl;

import com.rafael.peteventos.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.group.idGroup = ?1")
    List<Event> findByIdGroup(Long idGroup);
}
