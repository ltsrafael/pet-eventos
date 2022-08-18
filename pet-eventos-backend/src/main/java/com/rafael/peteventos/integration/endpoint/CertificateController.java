package com.rafael.peteventos.integration.endpoint;

import com.rafael.peteventos.domain.service.CertificateService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
@Api
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/generate")
    private ResponseEntity<?> generateCertificatesForEvent(@RequestParam Long idEvent) {
        certificateService.generateCertificatesForEvent(idEvent);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    private ResponseEntity<?> getCertificateForUserOnEvent(@RequestParam Long idEvent, @RequestParam Long idUser) {
        certificateService.generateCertificatesForUserOnEvent(idEvent, idUser);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/validate")
    private ResponseEntity<?> validateCertificate(@RequestParam String certificate) {
        return ResponseEntity.ok().body(certificateService.validateCertificate(certificate));
    }

    @GetMapping("/download")
    private ResponseEntity<?> downloadCertificate(@RequestParam String certificate) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(certificateService.downloadCertificate(certificate));
    }



}
