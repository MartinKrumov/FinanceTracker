package com.tracker.service.impl;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.domain.User;
import com.tracker.repository.UserRepository;
import com.tracker.service.AuthorityService;
import com.tracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServiceImplUnitTest {

    private static final long ID = 1L;
    private static final String USERNAME = "username";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityService authorityService;
    @Mock
    private PasswordEncoder encoder;

    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userService = new UserServiceImpl(userRepository, authorityService, encoder);

        user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    @DisplayName("Valid user")
    void loadByUsername_ReturnsValidUser() {
        //arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));

        //act
        UserDetails result = userService.loadUserByUsername(USERNAME);

        //assert
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("Username not found")
    void loadByUsername_ThrowsUsernameNotFoundException() {
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("Invalid"));

        assertEquals( "Invalid username", exception.getMessage());
    }

    @Test
    void findByIdOrThrow_GivenValidId_ReturnsUser() {
        //arrange
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));

        User result = userService.findByIdOrThrow(ID);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void findByIdOrThrow_GivenInvalidId_ThrowsException() {
        assertThrows(NoSuchElementException.class, () -> userService.findByIdOrThrow(2L));
    }

    @Test
    void checkIfExist_GivenExistingEmail_ThrowsException() {
        when(userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail()))
                .thenReturn(Boolean.TRUE);

        Exception exception = assertThrows(EntityAlreadyExistException.class,
                () ->  userService.register(user));

        assertEquals( "User already exists.", exception.getMessage());
    }

    @Test
    void register_GivenValidUser_SavesUser() {
        when(userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail()))
                .thenReturn(Boolean.FALSE);

        userService.register(user);
    }

    @Test
    void findByUsernameOrEmail_GivenValidCredentials_ReturnsUser() {
        when(userRepository.findByUsernameOrEmail(any(), any()))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsernameOrEmail(EMAIL);

        assertEquals(user.getEmail(), result.getEmail());
    }
}
