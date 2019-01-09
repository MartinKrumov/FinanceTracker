package com.financetracker.area.user.services.impl;

import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.models.UserRegistrationModel;
import com.financetracker.area.user.repositories.UserRepository;
import com.financetracker.area.user.services.AuthorityService;
import com.financetracker.area.user.services.UserService;
import com.financetracker.enums.CustomEntity;
import com.financetracker.exception.EntityAlreadyExistException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
        userRepository.findByUsernameAndEmail(newUser.getUsername(), newUser.getEmail())
                .orElseThrow(() -> new EntityAlreadyExistException(CustomEntity.USER));

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        var user = mapper.map(newUser, User.class);

        var authority = this.authorityService.getUserRole();
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
    public User findOneOrThrow(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}
