package com.rafael.peteventos.integration.endpoint;

import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.rafael.peteventos.application.security.JwtRequest;
import com.rafael.peteventos.domain.dto.GoogleApiResponseDto;
import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.domain.entity.User;
import com.rafael.peteventos.domain.service.AuthenticatedUserService;
import com.rafael.peteventos.domain.service.UserService;
import com.rafael.peteventos.integration.openfeign.GoogleApi;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Api
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class UserController {

    private final UserService userService;
    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping("/byRegistration")
    private ResponseEntity<?> getUserByRegistration(@RequestParam Integer registration) {
        return ResponseEntity.ok().body(userService.getUserByRegistration(registration));
    }

    @GetMapping("/byName")
    private ResponseEntity<?> getUserByName(@RequestHeader String name) {
        return ResponseEntity.ok().body(userService.getUserByName(name));
    }

    @GetMapping
    private ResponseEntity<?> getUser(@RequestHeader String Authorization) {
        return ResponseEntity.ok().body(userService.getUser(Authorization));
    }

    @GetMapping("/all")
    private ResponseEntity<List<?>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/unauthenticated")
    private ResponseEntity<?> postUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.postUser(user));
    }

    @PutMapping("/authenticated")
    private ResponseEntity<?> putAuthenticatedUser(@RequestBody AuthenticatedUser user) {
        authenticatedUserService.putUser(user);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/authenticated")
    private ResponseEntity<?> postAuthenticatedUser(@RequestBody AuthenticatedUser authenticatedUser, @RequestHeader String Authorization) {
        authenticatedUserService.SignUpUser(authenticatedUser, Authorization);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/authenticated/authenticate")
    private ResponseEntity<?> getAuthentication(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok().body(authenticatedUserService.authenticate(jwtRequest));
    }

    @GetMapping("/authenticated/validate")
    private ResponseEntity<?> validateToken(@RequestHeader String Authorization) {
        return ResponseEntity.ok().body(authenticatedUserService.validate(Authorization));
    }

    @GetMapping("/authenticated/reset")
    private ResponseEntity<?> resetPassword(@RequestParam String email) {
        authenticatedUserService.resetPassword(email);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/authenticated/authenticate/google")
    public ResponseEntity<?> getGoogleAuthentication(@RequestParam String accessToken) {
        return ResponseEntity.ok().body(authenticatedUserService.authenticateGoogle(accessToken));
    }
}
