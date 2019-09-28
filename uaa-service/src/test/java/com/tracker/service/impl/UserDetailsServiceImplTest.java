package com.tracker.service.impl;

import com.tracker.domain.User;
import com.tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Martin Krumov
 */
class UserDetailsServiceImplTest {

    private static final String USERNAME = "username";

    @Mock
    private UserRepository userRepository;

    private User user;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userRepository);

        user = User.builder()
                .username(USERNAME)
                .password(UUID.randomUUID().toString())
                .isAccountLocked(false)
                .isEnabled(true)
                .roles(new HashSet<>())
                .build();
    }

    @Test
    @DisplayName("Valid user")
    void loadByUsernameReturnsValidUser() {
        //arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));

        //act
        UserDetails result = userDetailsService.loadUserByUsername(USERNAME);

        //assert
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    @DisplayName("Username not found")
    void loadByUsernameThrowsUsernameNotFoundException() {
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("Invalid"));

        assertEquals( "User not found.", exception.getMessage());
    }
}
