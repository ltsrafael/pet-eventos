package com.rafael.peteventos.application.security;

import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.integration.repository.impl.AuthenticatedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthenticatedUserRepository authenticatedUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthenticatedUser> user = authenticatedUserRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Cadastro não encontrado para o email " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(),
                new ArrayList<>());
    }

}
