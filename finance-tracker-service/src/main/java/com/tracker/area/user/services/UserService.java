package com.tracker.area.user.services;


import com.tracker.area.user.domain.User;
import com.tracker.area.user.models.UserRegistrationModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);

    void register(UserRegistrationModel newUser);

    User findOneOrThrow(Long userId);
}
