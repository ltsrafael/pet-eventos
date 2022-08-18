package com.rafael.peteventos.integration.repository.impl;

import com.rafael.peteventos.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByName(String name);

    Optional<User> findUserByRegistration(Integer registration);

    Optional<User> findByEmail(String email);

}
