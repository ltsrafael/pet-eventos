package com.rafael.peteventos.integration.endpoint;


import com.rafael.peteventos.domain.entity.Attendance;
import com.rafael.peteventos.domain.service.AttendanceService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@AllArgsConstructor
@Api
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<?> postAttendances(@RequestBody List<Attendance> attendances, @RequestParam Long idSection) {
        attendanceService.postAttendances(attendances, idSection);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    public ResponseEntity<?> getAttendancesByEvent(@RequestParam Long idEvent) {
        return ResponseEntity.ok().body(attendanceService.getAttendancesByEvent(idEvent));
    }
}
