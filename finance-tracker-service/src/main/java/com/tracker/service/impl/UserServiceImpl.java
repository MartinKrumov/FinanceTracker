package com.tracker.service.impl;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.domain.Authority;
import com.tracker.domain.User;
import com.tracker.dto.user.UserRegistrationModel;
import com.tracker.repository.UserRepository;
import com.tracker.service.AuthorityService;
import com.tracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityService authorityService, BCryptPasswordEncoder encoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void register(UserRegistrationModel newUser) {
        checkIfExistsOrThrow(newUser.getUsername(), newUser.getEmail());

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        User user = mapper.map(newUser, User.class);

        Authority authority = this.authorityService.getUserRole();
        user.getAuthorities().add(authority);
        user.setDate(LocalDateTime.now(Clock.systemUTC()));

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User"));
    }

    @Override
    public User findByIdOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
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
