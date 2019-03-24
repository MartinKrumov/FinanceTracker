package com.tracker.service;


import com.tracker.domain.User;
import com.tracker.dto.user.UserRegistrationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);

    void register(UserRegistrationModel newUser);

    User findByIdOrThrow(Long userId);

    Page<User> findAll(Pageable pageable);
}
