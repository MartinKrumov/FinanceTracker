package com.tracker.repository;

import com.tracker.domain.Token;
import com.tracker.domain.User;
import com.tracker.domain.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Exposes operations on the {@link User} domain entity.
 *
 * @see User
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds an user by username.
     *
     * @param username the username of the user
     * @return the user or {@link Optional#empty()} if none found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds an user by email (case insensitive).
     *
     * @param email the email of the user
     * @return the user or {@link Optional#empty()} if none found
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Checks if user exists by username or email exists.
     *
     * @param username the username
     * @param email    the email
     * @return true if an user exists, false otherwise.
     */
    Boolean existsByUsernameOrEmail(String username, String email);

    /**
     * Finds an user by email or username.
     *
     * @param username the username of the user
     * @param email    the email of the user
     * @return the user or {@link Optional#empty()} if none found
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Finds an user by {@link Token#tokenType} and by {@link Token#code}
     *
     * @param tokenType {@link TokenType}
     * @param code      the code of the {@link Token}
     * @return the user or {@link Optional#empty()} if none found
     */
    Optional<User> findByTokens_TokenTypeAndTokens_Code(TokenType tokenType, String code);
}
