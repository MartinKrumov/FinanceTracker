package com.financetracker.area.user.services;


import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.models.UserRegistrationModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register(UserRegistrationModel newUser);

    User findOneOrThrow(Long userId);
}
