package com.financetracker.area.user.services;


import com.financetracker.area.user.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register(UserRegistrationDto newUser) throws Exception;
}
