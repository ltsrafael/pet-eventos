package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.CertificateDto;
import com.rafael.peteventos.domain.entity.Certificate;
import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.entity.Section;
import com.rafael.peteventos.domain.entity.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

public class CertificateDtoConverter {

    public static CertificateDto convertToCertificateDto(User user, Event event, Certificate certificate) {
        return CertificateDto.builder()
                .withCenter(event.getGroup().getCenter())
                .withCourse(event.getGroup().getCourse())
                .withEventName(event.getName())
                .withNumber(certificate.getNumber())
                .withParticipantName(user.getName())
                .withDates(getEventDates(event.getSections()))
                .withHours(getHours(event))
                .build();
    }

    private static String getHours(Event event) {
        long hours;
        hours = event.getSections().stream().mapToLong(section -> ChronoUnit.HOURS.between(section.getStart(), section.getEnd())).sum();
        if(hours <= 1) {
            return " ".concat(String.valueOf(hours)).concat(" hora");
        } else {
            return " ".concat(String.valueOf(hours)).concat(" horas");
        }
    }

    public static String getEventDates(List<Section> sections) {
        if(sections.size() == 1) {
            String days = " no dia ";
            String x =  days.concat(String.valueOf(sections.get(0).getStart().getDayOfMonth())).concat(getMonth(sections.get(0).getStart().getMonth()).concat(" de ").concat(String.valueOf(sections.get(0).getStart().getYear())));
            return x;
        } else {
            HashSet<String> eventDays = new HashSet<>();
            sections.forEach(section -> eventDays.add(getEventDay(section.getStart())));
            HashSet<String> eventMonths = new HashSet<>();
            sections.forEach(section -> eventMonths.add(getMonth(section.getStart().getMonth())));
            if(eventMonths.size() == 1) {
                if(eventDays.size() == 1) {
                    return " no dia ".concat(String.join(", ", eventDays)).concat(getMonth(sections.get(0).getStart().getMonth()).concat(" de ").concat(String.valueOf(sections.get(0).getStart().getYear())));
                } else {
                    return " nos dias ".concat(String.join(", ", eventDays)).concat(getMonth(sections.get(0).getStart().getMonth()).concat(" de ").concat(String.valueOf(sections.get(0).getStart().getYear())));
                }
            } else {
                AtomicReference<String> days = new AtomicReference<>(" nos dias ");
                List<String> dates = new ArrayList<>();
                sections.forEach(section -> {
                    dates.add(getEventDay(section.getStart()).concat(getMonth(section.getStart().getMonth())));
                });
                AtomicInteger x = new AtomicInteger(1);
                dates.forEach(date -> {
                    if(x.get() == dates.size()) {
                        days.set(days.get().concat(" e ").concat(date).concat(" de ").concat(String.valueOf(sections.get(0).getStart().getYear())));
                    } else if(x.get() == dates.size()-1){
                        days.set(days.get().concat(date));
                    } else {
                        days.set(days.get().concat(date).concat(", "));
                    }
                    x.addAndGet(1);
                });
                return days.get();
            }
        }
    }

    private static String getMonth(Month month) {
        switch (month) {
            case JANUARY:
                return " de Janeiro";
            case FEBRUARY:
                return " de Fevereiro";
            case MARCH:
                return " de Março";
            case APRIL:
                return " de Abril";
            case MAY:
                return " de Maio";
            case JUNE:
                return " de Junho";
            case JULY:
                return " de Julho";
            case AUGUST:
                return " de Agosto";
            case SEPTEMBER:
                return " de Setembro";
            case OCTOBER:
                return " de Outubro";
            case NOVEMBER:
                return " de Novembro";
            case DECEMBER:
                return " de Dezembro";
            default:
                return "ERROR";
        }
    }

    public static String getEventDay(LocalDateTime start) {
        return String.valueOf(start.getDayOfMonth());
    }
}
