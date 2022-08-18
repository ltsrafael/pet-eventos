package com.rafael.peteventos.domain.service;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.DocumentException;
import com.rafael.peteventos.domain.dto.CertificateDto;
import com.rafael.peteventos.domain.entity.Attendance;
import com.rafael.peteventos.domain.entity.Certificate;
import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.entity.User;
import com.rafael.peteventos.integration.repository.impl.CertificateRepository;
import com.rafael.peteventos.integration.repository.impl.EventRepository;
import com.rafael.peteventos.integration.repository.impl.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.rafael.peteventos.domain.converter.CertificateDtoConverter.convertToCertificateDto;
import static org.thymeleaf.templatemode.TemplateMode.HTML;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final EventRepository eventRepository;
    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String UTF_8 = "UTF-8";
    private static final String PDF = ".pdf";

    public void generateCertificatesForEvent(Long idEvent) {
        Optional<Event> eventOptional = eventRepository.findById(idEvent);
        if(eventOptional.isPresent()) {
            List<User> elegibleUsers = new ArrayList<>();
            Event event = eventOptional.get();
            List<User> users = event.getUsers();
            long hours;
            hours = event.getSections().stream().mapToLong(section -> ChronoUnit.HOURS.between(section.getStart(), section.getEnd())).sum();
            users.forEach(user -> {
                List<Attendance> attendanceList = user.getAttendances().stream().filter(attendance -> attendance.getEvent().getIdEvent() == event.getIdEvent()).collect(Collectors.toList());
                long userAttendance;
                userAttendance = attendanceList.stream().mapToLong(attendance -> ChronoUnit.HOURS.between(attendance.getSection().getStart(), attendance.getSection().getEnd())).sum();
                if(userAttendance >= (hours*0.75)) {
                    elegibleUsers.add(user);
                }
            });
            if(!elegibleUsers.isEmpty()) {
                elegibleUsers.forEach(user -> {
                    try {
                        generateCertificate(user, event);
                        updateEventStatus(event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                });
            }
            else {
                updateEventStatus(event);
            }

        }
    }

    public ByteArrayResource downloadCertificate(String certificate) {
        Optional<Certificate> optionalCertificate = certificateRepository.findByNumber(certificate);
        if (optionalCertificate.isPresent()) {
            try {
                Certificate validCertificate = optionalCertificate.get();
                ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
                templateResolver.setPrefix("/");
                templateResolver.setSuffix(".html");
                templateResolver.setTemplateMode(HTML);
                templateResolver.setCharacterEncoding(UTF_8);

                TemplateEngine templateEngine = new TemplateEngine();
                templateEngine.setTemplateResolver(templateResolver);

                CertificateDto certificateDto = convertToCertificateDto(validCertificate.getUser(), validCertificate.getEvent(), validCertificate);

                Context context = new Context();
                context.setVariable("certificateDto", certificateDto);
                ByteArrayOutputStream target = new ByteArrayOutputStream();
                String renderedHtmlContent = templateEngine.process("template", context);
                HtmlConverter.convertToPdf(renderedHtmlContent, target);

                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream("certificado.pdf");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                target.writeTo(outputStream);
                Path path = Paths.get("certificado.pdf");
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
                return resource;
            } catch (Exception e) {
                throw new RuntimeException("Certificado não encontrado.");
            }
        } else {
            throw new RuntimeException("Certificado não encontrado.");
        }
    }

    private void updateEventStatus(Event event) {
        event.setActive(false);
        eventRepository.save(event);
    }

    public void generateCertificatesForUserOnEvent(Long idEvent, Long idUser) {
        Optional<User> optionalUser = userRepository.findById(idUser);
        Optional<Event> optionalEvent = eventRepository.findById(idEvent);
        if(optionalEvent.isPresent() && optionalUser.isPresent()) {
            try {
                generateCertificate(optionalUser.get(), optionalEvent.get());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public CertificateDto validateCertificate(String certificate) {
        Optional<Certificate> optionalCertificate = certificateRepository.findByNumber(certificate);
        if(optionalCertificate.isPresent()) {
            try {
                return convertToCertificateDto(optionalCertificate.get().getUser(),optionalCertificate.get().getEvent(),optionalCertificate.get());
            } catch (Exception e) {
                throw new RuntimeException("Certificado não encontrado.");
            }
        }
        throw new RuntimeException("Certificado não encontrado.");

    }

    private void generateCertificate(User user, Event event) throws IOException, DocumentException {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);

        Certificate certificate = Certificate.builder()
                .withNumber(getNumber())
                .withEvent(Event.builder().withIdEvent(event.getIdEvent()).build())
                .withUser(User.builder().withIdUser(user.getIdUser()).build())
                .build();

        certificate = certificateRepository.save(certificate);
        CertificateDto certificateDto = convertToCertificateDto(user, event, certificate);

        Context context = new Context();
        context.setVariable("certificateDto", certificateDto);
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        String renderedHtmlContent = templateEngine.process("template", context);
        HtmlConverter.convertToPdf(renderedHtmlContent, target);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream("certificado.pdf");
            target.writeTo(outputStream);
            emailService.sendWithFile(user.getEmail(),"mail@gmail.com",  "certificado.pdf", event.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getNumber() {
        Optional<Certificate> optionalCertificate;
        String number;
        do {
            number = UUID.randomUUID().toString();
            optionalCertificate = certificateRepository.findByNumber(number);
        } while (optionalCertificate.isPresent());
        return number;
    }
}

