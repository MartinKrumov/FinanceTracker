package com.financetracker.area.user.services.impl;

import com.financetracker.area.user.domain.Authority;
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

import javax.transaction.Transactional;

@Service
@Transactional
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
    @Transactional
    public void register(UserRegistrationModel newUser) {
        userRepository.findByUsernameAndEmail(newUser.getUsername(), newUser.getEmail())
                .orElseThrow(() -> new EntityAlreadyExistException(CustomEntity.USER));

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        var user = mapper.map(newUser, User.class);

        Authority authority = this.authorityService.getUserRole();
        user.getAuthorities().add(authority);

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User"));
    }
}
