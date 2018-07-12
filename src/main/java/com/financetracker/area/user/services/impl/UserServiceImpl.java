package com.financetracker.area.user.services.impl;

import com.financetracker.area.user.domain.Authority;
import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.exceptions.UserAlreadyExists;
import com.financetracker.area.user.models.UserRegistrationModel;
import com.financetracker.area.user.repositories.UserRepository;
import com.financetracker.area.user.services.AuthorityService;
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
    private final AuthorityService authorityService;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Override
    public void register(UserRegistrationModel newUser) {
        boolean alreadyExist = checkIfUserExist(newUser);

        if (alreadyExist) {
            throw new UserAlreadyExists("The User already exists");
        }

        newUser.setPassword(encoder.encode(newUser.getPassword()));

        User user = mapper.map(newUser, User.class);

        Authority authority = this.authorityService.getUserRole();
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

    private boolean checkIfUserExist(UserRegistrationModel newUser) {
        return userRepository.findByUsernameAndEmail(newUser.getUsername(), newUser.getEmail()) != null;
    }
}
