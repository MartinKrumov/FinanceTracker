package com.tracker.service.impl;

import com.tracker.config.IdpProperties;
import com.tracker.domain.PreviousPassword;
import com.tracker.domain.Role;
import com.tracker.domain.Token;
import com.tracker.domain.User;
import com.tracker.domain.enums.TokenType;
import com.tracker.domain.enums.UserRole;
import com.tracker.repository.UserRepository;
import com.tracker.service.NotificationService;
import com.tracker.service.RoleService;
import com.tracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Martin Krumov
 */
class UserServiceImplUnitTest {

    private static final long ID = 1L;
    private static final String EMAIL = "test@test.com";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String PWD = UUID.randomUUID().toString();
    private static final String VERIFICATION_CODE = UUID.randomUUID().toString();
    private static final String RESET_CODE = UUID.randomUUID().toString();
    private static final int DAYS = 2;
    private static final int PASSWORD_HISTORY_LIMIT = 2;

    @Mock
    private RoleService roleService;
    @Mock
    private IdpProperties idpProperties;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationService notificationService;

    private User user;
    private UserService userService;

    private Map<TokenType, Duration> tokenTypeToValidity;
    private Token resetToken = buildToken(RESET_CODE, buildInstantPlusDays(0L), TokenType.RESET);
    private Token verificationToken = buildToken(VERIFICATION_CODE, buildInstantPlusDays(0L), TokenType.VERIFICATION);

    @BeforeEach
    void setUp() {
        initMocks(this);
        userService = new UserServiceImpl(roleService, idpProperties, userRepository, passwordEncoder, notificationService);

        user = User.builder()
                .id(ID)
                .email(EMAIL)
                .password(PWD)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .isEnabled(true)
                .isVerified(false)
                .tokens(new HashSet<>())
                .passwordHistory(new HashSet<>())
                .build();

        tokenTypeToValidity = new HashMap<>();
        tokenTypeToValidity.put(TokenType.RESET, Duration.ofDays(DAYS));

        when(idpProperties.getTokenTypeToValidity()).thenReturn(tokenTypeToValidity);
    }

    @Test
    @DisplayName("Register successfully saves user.")
    void registerSavesUser() {
        //arrange
        when(roleService.findByUserRole(UserRole.USER)).thenReturn(new Role(ID, UserRole.USER));
        when(userRepository.save(user)).thenReturn(user);

        //act
        User result = userService.register(user);

        //act
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getIsVerified(), result.getIsVerified());
        assertFalse(user.getPasswordHistory().isEmpty());
        assertTrue(user.getTokens().stream().anyMatch(t -> TokenType.VERIFICATION.equals(t.getTokenType())));

        Token token = result.getTokens().stream().findFirst().get();
        verify(notificationService).sendVerificationEmail(user.getEmail(), token.getCode());
    }

    @Test
    @DisplayName("Validate completeRegistration() is setting user as verified.")
    void completeRegistration() {
        // arrange
        user.getTokens().add(verificationToken);
        tokenTypeToValidity.put(TokenType.VERIFICATION, Duration.ofDays(DAYS));

        when(userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.VERIFICATION, VERIFICATION_CODE))
                .thenReturn(Optional.of(user));

        // act
        userService.completeRegistration(VERIFICATION_CODE);

        // assert
        assertEquals(Boolean.TRUE, user.getIsVerified());
        assertTrue(user.getTokens().stream().noneMatch(t -> TokenType.VERIFICATION.equals(t.getTokenType())));
    }

    @Test
    @DisplayName("Throw exception if the token is expired.")
    void completeRegistrationThrowsExceptionWhenTokenExpires() {
        //arrange
        user.getTokens().add(verificationToken);
        tokenTypeToValidity.put(TokenType.VERIFICATION, Duration.ofDays(DAYS).minusDays(5));

        when(userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.VERIFICATION, VERIFICATION_CODE))
                .thenReturn(Optional.of(user));
        //act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.completeRegistration(VERIFICATION_CODE));

        // assert
        assertFalse(user.getIsVerified());
        assertEquals(TokenType.VERIFICATION + " token is expired.", exception.getMessage());
        assertTrue(user.getTokens().stream().anyMatch(t -> TokenType.VERIFICATION.equals(t.getTokenType())));
    }

    @Test
    void checkIfExistGivenExistingEmailThrowsException() {
        when(userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail()))
                .thenReturn(Boolean.TRUE);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () ->  userService.register(user));

        assertEquals( "User already exists.", exception.getMessage());
    }

    @Test
    void resetPasswordGeneratesResetToken() {
        when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));

        //act
        userService.resetPassword(EMAIL);

        //assert
        assertTrue(user.getTokens().stream().anyMatch(t -> TokenType.RESET.equals(t.getTokenType())));

        Token token = user.getTokens().stream().findFirst().get();
        verify(notificationService).sendPasswordResetEmail(EMAIL, token.getCode());
    }

    @Test
    void validateTokenGivenInvalidCodeThrowsException() {
        user.getTokens().add(resetToken);
        when(userRepository.findByTokens_Code(resetToken.getCode())).thenReturn(Optional.of(user));

        //act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.validateToken(UUID.randomUUID().toString()));

        //assert
        assertEquals("Token not valid.", exception.getMessage());
    }

    @Test
    void validateTokenGivenExpiredTokenThrowsException() {
        user.getTokens().add(verificationToken);
        tokenTypeToValidity.put(TokenType.VERIFICATION, Duration.ofDays(DAYS).minusDays(5));

        when(userRepository.findByTokens_Code(verificationToken.getCode())).thenReturn(Optional.of(user));

        //act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.validateToken(verificationToken.getCode()));

        //assert
        assertEquals(verificationToken.getTokenType() + " token is expired.", exception.getMessage());
    }

    @Test
    @DisplayName("Validate resetPassword() is saving new password with correct token and password.")
    void resetPasswordGivenValidTokenAndPasswordChangePassword() {
        // arrange
        user.getTokens().add(resetToken);
        user.getPasswordHistory().add(buildPreviousPassword(buildInstantPlusDays(1L), getRandomUUID()));
        String newPassword = getRandomUUID();

        when(userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.RESET, RESET_CODE)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword); // Encode the password plain.

        // act
        userService.completePasswordReset(RESET_CODE, newPassword);

        // assert
        assertEquals(newPassword, user.getPassword());
        assertFalse(user.getTokens().contains(resetToken));
        assertTrue(user.getPasswordHistory().stream().map(PreviousPassword::getPassword).anyMatch(newPassword::equals));
    }

    @Test
    @DisplayName("When password history is exceeded it removes the oldest password.")
    void resetPasswordAdjustCorrectlyPasswordHistory() {
        // arrange

        Set<PreviousPassword> previousPasswords = Set.of(
                buildPreviousPassword(buildInstantPlusDays(1L), getRandomUUID()),
                buildPreviousPassword(buildInstantPlusDays(2L), getRandomUUID())
        );

        user.getTokens().add(resetToken);
        user.getPasswordHistory().addAll(previousPasswords);
        String newPassword = getRandomUUID();

        when(userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.RESET, RESET_CODE)).thenReturn(Optional.of(user));
        when(idpProperties.getPreviousPasswordsLimit()).thenReturn(PASSWORD_HISTORY_LIMIT);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        // act
        userService.completePasswordReset(RESET_CODE, newPassword);

        // assert
        assertEquals(newPassword, user.getPassword());
        assertFalse(user.getTokens().contains(resetToken));
        assertEquals(user.getPasswordHistory().size(), PASSWORD_HISTORY_LIMIT);
        assertTrue(user.getPasswordHistory().stream().map(PreviousPassword::getPassword).anyMatch(newPassword::equals));
    }

    @Test
    @DisplayName("Throw exception if password is in password history.")
    void resetPasswordThrowsExceptionIfPasswordIsUsed() {
        // arrange
        String newPassword = getRandomUUID();
        Set<PreviousPassword> previousPasswords = Set.of(
                buildPreviousPassword(buildInstantPlusDays(1L), getRandomUUID()),
                buildPreviousPassword(buildInstantPlusDays(2L), newPassword) //the password exist
        );

        user.getTokens().add(resetToken);
        user.getPasswordHistory().addAll(previousPasswords);

        when(userRepository.findByTokens_TokenTypeAndTokens_Code(TokenType.RESET, RESET_CODE)).thenReturn(Optional.of(user));
        when(idpProperties.getPreviousPasswordsLimit()).thenReturn(PASSWORD_HISTORY_LIMIT);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        when(passwordEncoder.matches(newPassword, newPassword)).thenReturn(Boolean.TRUE);

        //act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.completePasswordReset(RESET_CODE, newPassword));

        // assert
        assertNotEquals(newPassword, user.getPassword());
        assertTrue(user.getTokens().contains(resetToken));
        assertEquals("The password does not meet the history requirements of the domain.", exception.getMessage());
    }

    private PreviousPassword buildPreviousPassword(Instant instant, String password) {
        return PreviousPassword.builder()
                .password(password)
                .createdAt(instant)
                .build();
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private Token buildToken(String code, Instant createdAt, TokenType tokenType) {
        return Token.builder()
                .code(code)
                .createdAt(createdAt)
                .tokenType(tokenType)
                .build();
    }

    private Instant buildInstantPlusDays(Long days) {
        return Instant.now()
                .plusSeconds(Duration.ofDays(days).toSeconds());
    }
}
