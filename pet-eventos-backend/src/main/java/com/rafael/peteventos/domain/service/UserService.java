package com.rafael.peteventos.domain.service;

import com.rafael.peteventos.domain.dto.UserDto;
import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.domain.entity.User;
import com.rafael.peteventos.domain.util.JwtUtil;
import com.rafael.peteventos.integration.repository.impl.AuthenticatedUserRepository;
import com.rafael.peteventos.integration.repository.impl.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static com.rafael.peteventos.domain.converter.UserDTOConverter.convertUser;
import static com.rafael.peteventos.domain.converter.UserDTOConverter.convertUserList;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final String USER_NOT_FOUND = "Usuário não encontrado";

    public UserDto getUser(String bearer) {
        Optional<AuthenticatedUser> optionalUser = authenticatedUserRepository.findByEmail(JwtUtil.getEmail(bearer));
        if(optionalUser.isPresent()) {
            return convertUser(optionalUser.get());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_FOUND);
        }
    }

    public List<UserDto> getUsers() {
        return convertUserList(authenticatedUserRepository.findAll());
    }

    public UserDto postUser(User user) {
        Optional<AuthenticatedUser> optionalAuthenticatedUser = authenticatedUserRepository.findByEmail(user.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        Optional<User> optionalUserByRegistration = userRepository.findUserByRegistration(user.getRegistration());

        if(optionalAuthenticatedUser.isPresent() || optionalUser.isPresent()) {
            throw new RuntimeException("Email já possui cadastro.");
        } else if(optionalUserByRegistration.isPresent()) {
            throw new RuntimeException("Matrícula já possui cadastro.");
        } else {
            return convertUser(userRepository.save(user));
        }
    }

    public void putUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getIdUser());
        if(optionalUser.isPresent()) {
            userRepository.save((User)user);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_FOUND);
        }
    }

    public UserDto getUserByRegistration(Integer registration) {
        Optional<User> optionalUser = userRepository.findUserByRegistration(registration);
        if(optionalUser.isPresent()) {
            return convertUser(optionalUser.get());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_FOUND);
        }
    }

    public UserDto getUserByName(String name) {
        Optional<User> optionalUser = userRepository.findUserByName(name);
        if(optionalUser.isPresent()) {
            return convertUser(optionalUser.get());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_FOUND);
        }
    }
}
