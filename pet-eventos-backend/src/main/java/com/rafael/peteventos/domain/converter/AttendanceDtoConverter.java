package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.AttendanceDto;
import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.entity.Section;
import com.rafael.peteventos.domain.entity.User;

import static com.rafael.peteventos.domain.converter.EventDTOConverter.convertEvent;
import static com.rafael.peteventos.domain.converter.SectionDTOConverter.convertSection;
import static com.rafael.peteventos.domain.converter.UserDTOConverter.convertUser;

public class AttendanceDtoConverter {

    public static AttendanceDto convertAttendance(User user, Event event, Section section) {
        return AttendanceDto.builder()
                .withEvent(convertEvent(event))
                .withSection(convertSection(section))
                .withUser(convertUser(user))
                .build();
    }

}
