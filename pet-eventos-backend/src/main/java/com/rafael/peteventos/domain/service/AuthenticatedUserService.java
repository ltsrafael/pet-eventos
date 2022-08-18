package com.rafael.peteventos.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafael.peteventos.application.security.JwtRequest;
import com.rafael.peteventos.application.security.JwtTokenUtil;
import com.rafael.peteventos.application.security.JwtUserDetailsService;
import com.rafael.peteventos.domain.dto.GoogleApiResponseDto;
import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.domain.entity.Group;
import com.rafael.peteventos.domain.entity.User;
import com.rafael.peteventos.domain.enums.UserType;
import com.rafael.peteventos.domain.util.JwtUtil;
import com.rafael.peteventos.integration.openfeign.GoogleApi;
import com.rafael.peteventos.integration.repository.impl.AuthenticatedUserRepository;
import com.rafael.peteventos.integration.repository.impl.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.rafael.peteventos.domain.converter.AuthDtoConverter.convertAuth;
import static com.rafael.peteventos.domain.enums.UserType.ADMIN;
import static com.rafael.peteventos.domain.enums.UserType.USER;
import static com.rafael.peteventos.domain.util.JwtUtil.getToken;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GoogleApi googleApi;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final String INVALID_BEARER = "Bearer token inválido.";
    private final String USER_NOT_FOUND = "Usuário não encontrado";


    public AuthenticatedUser SignUpUser(AuthenticatedUser user, String bearer) {
        Optional<AuthenticatedUser> authenticatedUserOptional = authenticatedUserRepository.findByEmail(JwtUtil.getEmail(bearer));
        String password = generatePassword();
        user.setPassword(password);
        if(authenticatedUserOptional.isPresent()) {
            Optional<AuthenticatedUser> authenticatedUserPresent = authenticatedUserRepository.findByEmail(user.getEmail());
            Optional<User> userOptionalPresent = userRepository.findByEmail(user.getEmail());
            Optional<User> userOptionalRegistration = userRepository.findUserByRegistration(user.getRegistration());
            if(authenticatedUserPresent.isPresent()) {
                throw new RuntimeException("Email já possui cadastro.");
            } else if(userOptionalPresent.isPresent()  || userOptionalRegistration.isPresent()) {
                if(userOptionalPresent.isPresent()) {
                    String userType = userOptionalPresent.get().getUserType();
                    if(userType == null) {
                        user.setIdUser(userOptionalPresent.get().getIdUser());
                        try {
                            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                        }
                        catch(Exception e) {
                            throw new RuntimeException("Erro encode senha.");
                        }
                        if(authenticatedUserOptional.get().getUserType().equals(ADMIN.name())) {
                            user = authenticatedUserRepository.save(user);
                            emailService.send(user.getEmail(), buildEmail(password,user.getEmail()));
                            return user;
                        }
                        user.setGroup(Group.builder().withIdGroup(authenticatedUserOptional.get().getGroup().getIdGroup()).build());
                        user.setUserType(USER.name());
                        userRepository.delete(userOptionalPresent.get());
                        user = authenticatedUserRepository.save(user);
                        emailService.send(user.getEmail(), buildEmail(password,user.getEmail()));
                        return user;
                    }
                } else if(userOptionalRegistration.isPresent()) {
                    String userType = userOptionalRegistration.get().getUserType();
                    if(userType == null) {
                        user.setIdUser(userOptionalRegistration.get().getIdUser());
                        try {
                            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                        }
                        catch(Exception e) {
                            throw new RuntimeException("Erro encode senha.");
                        }
                        if(authenticatedUserOptional.get().getUserType().equals(ADMIN.name())) {
                            user = authenticatedUserRepository.save(user);
                            emailService.send(user.getEmail(), buildEmail(password,user.getEmail()));
                            return user;
                        }
                        user.setGroup(Group.builder().withIdGroup(authenticatedUserOptional.get().getGroup().getIdGroup()).build());
                        user.setUserType(USER.name());
                        userRepository.delete(userOptionalPresent.get());
                        user = authenticatedUserRepository.save(user);
                        emailService.send(user.getEmail(), buildEmail(password,user.getEmail()));
                        return user;
                    } else {
                        throw new RuntimeException("Matrícula já possui cadastro.");
                    }
                } else {
                    throw new RuntimeException("Matrícula já possui cadastro.");
                }
            }  else {
                try {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                }
                catch(Exception e) {
                    throw new RuntimeException("Erro encode senha.");
                }
                if(authenticatedUserOptional.get().getUserType().equals(ADMIN.name())) {
                    emailService.send(user.getEmail(), buildEmail(password,user.getEmail()));
                    return authenticatedUserRepository.save(user);
                }
                user.setGroup(Group.builder().withIdGroup(authenticatedUserOptional.get().getGroup().getIdGroup()).build());
                user.setUserType(USER.name());
                user = authenticatedUserRepository.save(user);
                emailService.send(user.getEmail(), buildEmail(password,user.getEmail()));
                return user;
            }
        } else {
            throw new RuntimeException(INVALID_BEARER);
        }
        return user;
    }

    public void resetPassword(String email) {
        Optional<AuthenticatedUser> userOptional = authenticatedUserRepository.findByEmail(email);
        String password = generatePassword();
        if(!userOptional.isPresent()) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        try {
            userOptional.get().setPassword(bCryptPasswordEncoder.encode(password));
        }
        catch(NullPointerException e) {
            System.out.println("Erro ao criptografar senha.");
        }
        authenticatedUserRepository.save(userOptional.get());
        emailService.send(userOptional.get().getEmail(), buildEmail(password,userOptional.get().getEmail()));
    }

    private String buildEmail(String password, String email) {
        return "<p>Olá, dados para login: </p><p>Email : " + email + "</p><p>Senha : " + password + "</p>";
    }


    public ResponseEntity<Object> authenticate(JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(jwtRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        Optional<AuthenticatedUser> authenticatedUser = authenticatedUserRepository.findByEmail(jwtRequest.getEmail());


        return ResponseEntity.ok(convertAuth(token,authenticatedUser.get().getUserType()));
    }

    public ResponseEntity<Object> authenticateGoogle(String accessToken) {
        try {
            GoogleApiResponseDto response = googleApi.retornaUsuario(accessToken);
            if(Objects.nonNull(response)) {
                final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(response.getEmail());
                final String token = jwtTokenUtil.generateToken(userDetails);
                Optional<AuthenticatedUser> authenticatedUser = authenticatedUserRepository.findByEmail(response.getEmail());
                return ResponseEntity.ok(convertAuth(token,authenticatedUser.get().getUserType()));
            } else {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<Object> validate(String bearer) {
        try {
            DecodedJWT jwt = JWT.decode(getToken(bearer));
            return ResponseEntity.ok(jwt.getExpiresAt().after(new Date()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid token.");
        }
    }

    public void putUser(AuthenticatedUser user) {
        Optional<AuthenticatedUser> optionalUser = authenticatedUserRepository.findById(user.getIdUser());
        if(optionalUser.isPresent()) {
            user.setPassword(optionalUser.get().getPassword());
            authenticatedUserRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_FOUND);
        }
    }

    public String generatePassword() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
}
