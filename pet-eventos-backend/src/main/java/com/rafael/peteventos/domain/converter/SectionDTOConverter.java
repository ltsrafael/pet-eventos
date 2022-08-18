package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.SectionDto;
import com.rafael.peteventos.domain.entity.Section;

import java.util.List;
import java.util.stream.Collectors;


public class SectionDTOConverter {

    public static SectionDto convertSection(Section section) {
        return SectionDto.builder()
                .withName(section.getName())
                .withDescription(section.getDescription())
                .withStart(section.getStart())
                .withEnd(section.getEnd())
                .withIdSection(section.getIdSection())
                .build();
    }

    public static List<SectionDto> convertSectionList(List<Section> sections) {
        return sections.stream().map(SectionDTOConverter::convertSection).collect(Collectors.toList());
    }
}
