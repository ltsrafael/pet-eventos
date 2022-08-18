package com.rafael.peteventos.domain.service;

import com.rafael.peteventos.domain.dto.AttendanceDto;
import com.rafael.peteventos.domain.entity.Attendance;
import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.entity.Section;
import com.rafael.peteventos.integration.repository.impl.AttendanceRepository;
import com.rafael.peteventos.integration.repository.impl.EventRepository;
import com.rafael.peteventos.integration.repository.impl.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rafael.peteventos.domain.converter.AttendanceDtoConverter.convertAttendance;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final SectionRepository sectionRepository;
    private final EventRepository eventRepository;
    private final AttendanceRepository attendanceRepository;

    public void postAttendances(List<Attendance> attendances, Long idSection) {
        Optional<Section> optionalSection = sectionRepository.findById(idSection);
        if(optionalSection.isPresent()) {
            Section section = optionalSection.get();
            attendances.forEach(attendance -> section.getAttendances().add(attendance));
            sectionRepository.save(section);
        }
    }

    public List<AttendanceDto> getAttendancesByEvent(Long idEvent) {
        Optional<Event> optionalEvent = eventRepository.findById(idEvent);
        if(optionalEvent.isPresent()) {
            List<Attendance> attendances = attendanceRepository.findAttendanceByEvent(idEvent);
            return attendances.stream().map(attendance -> convertAttendance(attendance.getUser(), attendance.getEvent(), attendance.getSection())).collect(Collectors.toList());
        }
        else {
            throw new RuntimeException("Evento não encontrado.");
        }
    }
}
