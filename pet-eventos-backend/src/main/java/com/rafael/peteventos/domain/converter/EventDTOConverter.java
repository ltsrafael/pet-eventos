package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.EventDto;
import com.rafael.peteventos.domain.dto.SectionDto;
import com.rafael.peteventos.domain.dto.UserDto;
import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.entity.Section;
import com.rafael.peteventos.domain.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.rafael.peteventos.domain.converter.GroupDTOConverter.convertGroup;

@RequiredArgsConstructor
public class EventDTOConverter {

    public static EventDto convertEvent(Event event) {
        return EventDto.builder()
                .withIdEvent(event.getIdEvent())
                .withName(event.getName())
                .withDescription(event.getDescription())
                .withUsers(getUsers(event.getUsers()))
                .withSections(getSections(event.getSections()))
                .withGroup(convertGroup(event.getGroup()))
                .withPlace(event.getPlace())
                .withIsActive(event.isActive())
                .withIsCancelled(event.isCancelled())
                .build();
    }

    private static List<SectionDto> getSections(List<Section> sections) {
        return sections.stream().map(SectionDTOConverter::convertSection).collect(Collectors.toList());
    }

    public static List<EventDto> convertEventList(List<Event> events) {
        return events.stream().map(EventDTOConverter::convertEvent).collect(Collectors.toList());
    }

    private static List<UserDto> getUsers(List<User> users) {
        return users.stream().map(UserDTOConverter::convertUser).collect(Collectors.toList());
    }
}
