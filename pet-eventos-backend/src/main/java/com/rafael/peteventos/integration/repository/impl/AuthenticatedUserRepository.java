package com.rafael.peteventos.integration.repository.impl;

import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser, Long> {

    Optional<AuthenticatedUser> findByEmail(String email);
}
