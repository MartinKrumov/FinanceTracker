package com.tracker.service.impl;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.domain.Authority;
import com.tracker.domain.User;
import com.tracker.repository.UserRepository;
import com.tracker.service.AuthorityService;
import com.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final PasswordEncoder encoder;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void register(User user) {
        checkIfExistsOrThrow(user.getUsername(), user.getEmail());

        user.setPassword(encoder.encode(user.getPassword()));

        Authority authority = this.authorityService.getUserRole();
        user.getAuthorities().add(authority);
        user.setDate(LocalDateTime.now(Clock.systemUTC()));

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User"));

        return withUsername(username)
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    @Override
    public User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findByUsernameOrEmail(String credential) {
        return userRepository.findByUsernameOrEmail(credential, credential)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username/email."));
    }

    /**
     * Checks if the username or email email already exists.
     *
     * @param username username of the user
     * @param email email of the user
     */
    private void checkIfExistsOrThrow(String username, String email) {
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new EntityAlreadyExistException("User already exists.");
        }
    }
}
