package com.rafael.peteventos.integration.endpoint;

import com.rafael.peteventos.domain.service.SectionService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/section")
@AllArgsConstructor
@Api
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class SectionController {

    private final SectionService sectionService;

    @DeleteMapping
    private ResponseEntity<?> deleteSection(@RequestParam Long idSection) {
        sectionService.deleteSection(idSection);
        return ResponseEntity.ok().body(null);
    }
}
