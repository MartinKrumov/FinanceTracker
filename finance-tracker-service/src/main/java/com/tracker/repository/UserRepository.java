package com.tracker.repository;

import com.tracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
