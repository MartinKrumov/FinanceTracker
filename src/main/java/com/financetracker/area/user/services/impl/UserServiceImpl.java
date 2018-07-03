package com.financetracker.area.user.services.impl;

import com.financetracker.area.user.domain.Authority;
import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.dto.UserRegistrationDto;
import com.financetracker.area.user.exceptions.UserAlreadyExists;
import com.financetracker.area.user.repositories.AuthorityRepository;
import com.financetracker.area.user.repositories.UserRepository;
import com.financetracker.area.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Override
    public void register(UserRegistrationDto newUser) {
        boolean alreadyExist = checkIfUserExist(newUser);

        if (alreadyExist) {
            throw new UserAlreadyExists("The User already exists");
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        User user = new User();
        mapper.map(newUser, user);

        Authority authority = this.authorityRepository.findOneByAuthority("ROLE_USER");
        user.getAuthorities().add(authority);
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid User");
        }

        return user;
    }

    private boolean checkIfUserExist(UserRegistrationDto newUser) {
        return userRepository.findByUsername(newUser.getUsername()) != null;
    }
}
