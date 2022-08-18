package com.rafael.peteventos.domain.service;

import com.rafael.peteventos.domain.dto.EventDto;
import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.entity.Group;
import com.rafael.peteventos.domain.entity.User;
import com.rafael.peteventos.domain.util.JwtUtil;
import com.rafael.peteventos.integration.repository.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rafael.peteventos.domain.converter.EventDTOConverter.convertEvent;
import static com.rafael.peteventos.domain.converter.EventDTOConverter.convertEventList;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final UserRepository userRepository;
    private final SectionRepository sectionRepository;
    private final AttendanceService attendanceService;
    private final AttendanceRepository attendanceRepository;
    private final String USER_ALREADY_REGISTERED = "Usuário já se inscreveu para o evento.";
    private final String EVENT_NOT_FOUND = "Evento não encontrado";
    private final String INVALID_BEARER = "Bearer token inválido.";

    public EventDto getEvent(Long idEvent) {
        Optional<Event> optionalEvent = eventRepository.findById(idEvent);
        if(optionalEvent.isPresent()) {
            return convertEvent(optionalEvent.get());
        } else {
            throw new RuntimeException(EVENT_NOT_FOUND);
        }
    }

    public List<EventDto> getEvents() {
        List<EventDto> events = convertEventList(eventRepository.findAll());
        events.forEach(eventDto -> {
            eventDto.setAttendances(attendanceService.getAttendancesByEvent(eventDto.getIdEvent()));
        });
        return events;
    }

    public void postEvent(Event e, String bearer) {
        Optional<AuthenticatedUser> userOptional = authenticatedUserRepository.findByEmail(JwtUtil.getEmail(bearer));
        if(userOptional.isPresent()) {
            e.setGroup(Group.builder().withIdGroup(userOptional.get().getGroup().getIdGroup()).build());
            e.setActive(true);
            Event event = eventRepository.save(e);
            if(!Objects.isNull(e.getSections())) {
                e.getSections().forEach(section -> {
                    section.setEvent(Event.builder().withIdEvent(event.getIdEvent()).build());
                    sectionRepository.save(section);
                });
            }
        } else {
            throw new RuntimeException(INVALID_BEARER);
        }
    }


    public void putEvent(Event event) {
        Optional<Event> optionalEvent = eventRepository.findById(event.getIdEvent());
        if(optionalEvent.isPresent()) {
            attendanceRepository.deleteAttendanceByEvent(event.getIdEvent());
            eventRepository.save(event);
        } else {
            throw new RuntimeException(EVENT_NOT_FOUND);
        }
    }

    public void addUserToEvent(Long idUser, Long idEvent) {
        Optional<Event> optionalEvent = eventRepository.findById(idEvent);
        if(optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.getUsers().forEach(user -> {if(user.getIdUser().equals(idUser)) {
                throw new RuntimeException(USER_ALREADY_REGISTERED);
            }
            });
            event.getUsers().add(User.builder().withIdUser(idUser).build());
            eventRepository.save(event);
        } else {
            throw new RuntimeException(EVENT_NOT_FOUND);
        }
    }

    public List<EventDto> getEventByGroup(String bearer) {
        Optional<AuthenticatedUser> userOptional = authenticatedUserRepository.findByEmail(JwtUtil.getEmail(bearer));
        if(userOptional.isPresent()) {
            List<EventDto> events = convertEventList(eventRepository.findByIdGroup(userOptional.get().getGroup().getIdGroup()));
            events.forEach(eventDto -> {
                eventDto.setAttendances(attendanceService.getAttendancesByEvent(eventDto.getIdEvent()));
            });
            return events;
        } else {
            throw new RuntimeException(INVALID_BEARER);
        }
    }
}
