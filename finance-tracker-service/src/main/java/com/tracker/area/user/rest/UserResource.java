package com.tracker.area.user.rest;

import com.tracker.area.user.domain.User;
import com.tracker.area.user.mapper.UserMapper;
import com.tracker.area.user.model.UserInfoDTO;
import com.tracker.area.user.model.UserRegistrationModel;
import com.tracker.area.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UserRegistrationModel newUser) {
        log.info("Request for creating user has been received");
        userService.register(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserInfoDTO>> getUsers(Pageable pageable) {
        log.info("Request for retrieving page of users with pageNumber= {} and pageSize= {} has been received",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userService.findAll(pageable);
        List<UserInfoDTO> userInfoDTOS = userMapper.usersToUserInfoDTOs(users.getContent());

        var result = new PageImpl<>(userInfoDTOS, pageable, users.getTotalElements());
        return ResponseEntity.ok(result);
    }
}
