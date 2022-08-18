package com.rafael.peteventos.domain.service;


import com.rafael.peteventos.integration.repository.impl.SectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public void deleteSection(Long idSection) {
        sectionRepository.deleteById(idSection);
    }
}
