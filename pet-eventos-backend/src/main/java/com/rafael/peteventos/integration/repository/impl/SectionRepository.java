package com.rafael.peteventos.integration.repository.impl;

import com.rafael.peteventos.domain.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}

