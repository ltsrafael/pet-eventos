package com.rafael.peteventos.integration.endpoint;

import com.rafael.peteventos.domain.entity.Event;
import com.rafael.peteventos.domain.service.EventService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
@Api
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class EventController {

    private final EventService eventService;

    @GetMapping("/byId")
    private ResponseEntity<?> getEvent(@RequestParam Long idEvent) {
        return ResponseEntity.ok().body(eventService.getEvent(idEvent));
    }

    @GetMapping("/byGroup")
    private ResponseEntity<?> getEventByGroup(@RequestHeader String Authorization) {
        return ResponseEntity.ok().body(eventService.getEventByGroup(Authorization));
    }

    @GetMapping("/all")
    private ResponseEntity<List<?>> getEvents() {
        return ResponseEntity.ok().body(eventService.getEvents());
    }

    @PostMapping
    private ResponseEntity<?> postEvent(@RequestBody Event event, @RequestHeader String Authorization) {
        eventService.postEvent(event,Authorization);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping
    private ResponseEntity<?> putEvent(@RequestBody Event event) {
        eventService.putEvent(event);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/add/user")
    private ResponseEntity<?> addUserToEvent(@RequestParam Long idUser, @RequestParam Long idEvent) {
        eventService.addUserToEvent(idUser, idEvent);
        return ResponseEntity.ok().build();
    }
}
