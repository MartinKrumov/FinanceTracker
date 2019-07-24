package com.tracker.service;


import com.tracker.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User save(User user);

    User register(User user);

    User findByIdOrThrow(Long userId);

    Page<User> findAll(Pageable pageable);

    User findByUsernameOrEmail(String credential);
}
