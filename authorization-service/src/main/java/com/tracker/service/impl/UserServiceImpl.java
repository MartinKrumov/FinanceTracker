package com.tracker.service.impl;

import com.tracker.common.util.TimeUtils;
import com.tracker.config.IdpProperties;
import com.tracker.domain.PreviousPassword;
import com.tracker.domain.Role;
import com.tracker.domain.Token;
import com.tracker.domain.User;
import com.tracker.domain.enums.TokenType;
import com.tracker.repository.UserRepository;
import com.tracker.service.NotificationService;
import com.tracker.service.RoleService;
import com.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static org.springframework.util.Assert.isTrue;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleService roleService;
    private final IdpProperties idpProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User register(User user) {
        checkIfExistsOrThrow(user.getUsername(), user.getEmail());

        Instant now = Instant.now();
        Token activationToken = buildToken(UUID.randomUUID().toString(), now, TokenType.VERIFICATION);
        String encodedPwd = passwordEncoder.encode(user.getPassword());

        PreviousPassword previousPassword = PreviousPassword.builder()
                .password(encodedPwd)
                .createdAt(now)
                .build();

        Role role = roleService.getUserRole();
        user.setRoles(Set.of(role));
        user.setCreatedAt(LocalDateTime.now(Clock.systemUTC())); //TODO: research Instant vs LocalDateTime
        user.setIsEnabled(true);
        user.setIsVerified(false);
        user.setTokens(Set.of(activationToken));
        user.setPassword(encodedPwd);
        user.setPasswordHistory(Set.of(previousPassword));

        User saved = userRepository.save(user);
        log.debug("User {} registered successfully", saved);

        notificationService.sendVerificationEmail(saved.getEmail(), activationToken.getCode());
        return saved;
    }

    @Override
    @Transactional
    public void completeRegistration(String verificationCode) {
        User user = userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.VERIFICATION, verificationCode)
                .orElseThrow(() -> new IllegalArgumentException("Token not valid."));

        log.debug("User: {}", user);

        Token accessToken = getTokenByCode(verificationCode, user.getTokens());
        validateTokenIsNotExpired(accessToken, getTokenValidity(accessToken.getTokenType()).toSeconds());

        user.setIsVerified(true);
        user.getTokens().remove(accessToken);

        userRepository.save(user);
    }

    @Override
    public void validateToken(String tokenCode) {
        Token token = userRepository.findByTokens_Code(tokenCode)
                .map(u -> getTokenByCode(tokenCode, u.getTokens()))
                .orElseThrow(() -> new IllegalArgumentException("Token not valid."));

        Duration validity = getTokenValidity(token.getTokenType());

        validateTokenIsNotExpired(token, validity.toSeconds());
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for given email"));

        Token resetToken = buildToken(UUID.randomUUID().toString(), Instant.now(), TokenType.RESET);
        user.getTokens().add(resetToken);

        userRepository.save(user);

        log.info("Initiated password reset for user: {}", email);
        notificationService.sendPasswordResetEmail(email, resetToken.getCode());
    }

    @Override
    @Transactional
    public void completePasswordReset(String resetCode, String password) {
        User user = userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.RESET, resetCode)
                .orElseThrow(() -> new IllegalArgumentException("Token not found."));

        Token resetToken = getTokenByCode(resetCode, user.getTokens());
        validateTokenIsNotExpired(resetToken, getTokenValidity(resetToken.getTokenType()).toSeconds());

        validatePasswordIsNotUsedBefore(password, user.getPasswordHistory());

        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);
        user.getTokens().remove(resetToken);
        adjustPasswordHistory(encodedPassword, user.getPasswordHistory());

        log.info("Changing the password of user: {}", user.getEmail());
        userRepository.save(user);
    }

    @Override
    public void lockByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        log.debug("Locking user account: {}", username);

        user.setIsAccountLocked(true);
        userRepository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Gets token by code.
     *
     * @param code the code of the {@link Token}
     * @param tokens users tokens
     * @return the {@link Token} from the collection
     * @throws IllegalArgumentException if the token is not found
     */
    private Token getTokenByCode(String code, Set<Token> tokens) {
        return tokens.stream()
                .filter(token -> Objects.equals(token.getCode(), code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Token not found."));
    }

    /**
     * Validates the password is not used before.
     *
     * @param token {@link Token}
     * @param seconds seconds to be add to {@link Instant#now()}
     * @throws IllegalArgumentException if the token is expired
     */
    private void validateTokenIsNotExpired(Token token, Long seconds) {
        isTrue(TimeUtils.isBefore(token.getCreatedAt(), seconds), token.getTokenType() + " token is expired.");
    }

    /**
     * Validates the password is not used before.
     *
     * @param password          password of the user
     * @param previousPasswords previous passwords
     * @throws IllegalArgumentException if the password is used before
     */
    private void validatePasswordIsNotUsedBefore(String password, Set<PreviousPassword> previousPasswords) {
        boolean isUsed = previousPasswords.stream()
                .map(PreviousPassword::getPassword)
                .anyMatch(encodedPwd -> passwordEncoder.matches(password, encodedPwd));

        if (isUsed) {
            throw new IllegalArgumentException("The password does not meet the history requirements of the domain.");
        }
    }

    /**
     * Adds the new password to the password history.
     * <p>If the password history reaches the limit it removes the oldest password.</p>
     *
     * @param encodedPassword encoded password
     * @param passwordHistory previous passwords
     */
    private void adjustPasswordHistory(String encodedPassword, Set<PreviousPassword> passwordHistory) {
        if (Objects.equals(passwordHistory.size(), idpProperties.getPreviousPasswordsLimit())) {
            passwordHistory.stream()
                    .min(comparing(PreviousPassword::getCreatedAt))
                    .ifPresent(passwordHistory::remove);
        }

        PreviousPassword previousPassword = PreviousPassword.builder()
                .password(encodedPassword)
                .createdAt(Instant.now())
                .build();

        passwordHistory.add(previousPassword);
    }

    /**
     * Checks if the username or email email already exists.
     *
     * @param username username of the user
     * @param email email of the user
     */
    private void checkIfExistsOrThrow(String username, String email) {
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new IllegalStateException("User already exists.");
        }
    }

    /**
     * Get duration for given {@link TokenType} from {@link IdpProperties#getTokenTypeToValidity()}
     *
     * @param tokenType the token type
     * @return {@link Duration} validity for given token type
     */
    private Duration getTokenValidity(TokenType tokenType) {
        return idpProperties.getTokenTypeToValidity().get(tokenType);
    }

    private Token buildToken(String code, Instant createdAt, TokenType tokenType) {
        return Token.builder()
                .code(code)
                .createdAt(createdAt)
                .tokenType(tokenType)
                .build();
    }
}
