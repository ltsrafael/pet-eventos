package com.rafael.peteventos.integration.endpoint;

import com.rafael.peteventos.domain.entity.Group;
import com.rafael.peteventos.domain.service.GroupService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
@Api
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    private ResponseEntity<?> getGroup(@RequestHeader String Authorization) {
        return ResponseEntity.ok().body(groupService.getGroup(Authorization));
    }

    @GetMapping("/all")
    private ResponseEntity<List<?>> getGroups() {
        return ResponseEntity.ok().body(groupService.getGroups());
    }

    @PostMapping
    private ResponseEntity<?> postGroup(@RequestBody Group group) {
        groupService.postGroup(group);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping
    private ResponseEntity<?> putGroup(@RequestBody Group group) {
        groupService.putGroup(group);
        return ResponseEntity.ok().body(null);
    }
}
