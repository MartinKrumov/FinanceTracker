package com.tracker.service.impl;

import com.tracker.domain.Role;
import com.tracker.domain.User;
import com.tracker.repository.UserRepository;
import com.tracker.service.RoleService;
import com.tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User register(User user) {
        checkIfExistsOrThrow(user.getUsername(), user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = this.roleService.getUserRole();
        user.setRoles(Set.of(role));
        user.setCreatedAt(LocalDateTime.now(Clock.systemUTC())); //TODO: research Instant and Set.of()
        user.setIsEnabled(true);
        user.setIsVerified(false);

        //TODO: ActivationKey/Token, Pass history

        return userRepository.save(user);
        //TODO send verification email
    }



    @Override
    public User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
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
            throw new IllegalStateException("User already exists.");
        }
    }
}
