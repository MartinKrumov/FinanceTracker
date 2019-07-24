package com.tracker.rest;

import com.tracker.domain.User;
import com.tracker.mapper.UserMapper;
import com.tracker.rest.dto.user.UserInfoDTO;
import com.tracker.rest.dto.user.UserRegisterDTO;
import com.tracker.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserMapper userMapper;
    private final UserService userService;

    @ApiOperation(value = "Register user")
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Creating user: {}", userRegisterDTO);
        User user = userService.register(userMapper.toUser(userRegisterDTO));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDtoIgnorePassword(user));
    }

    @ApiOperation(value = "Complete user registration.",
            notes = "Verifies users account.")
    @PostMapping("/complete-register")
    public ResponseEntity completeRegister(@RequestParam("token") String token) {
        log.info("Completing registration with token: {}", token);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Retrieve collection of users",
            notes = "Retrieve users for given page number, size and sort.")
    @GetMapping
    public ResponseEntity<Page<UserInfoDTO>> getUsers(Pageable pageable) {
        log.info("Request for retrieving page of users with pageNumber= {} and pageSize= {} has been received",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userService.findAll(pageable);
        List<UserInfoDTO> userInfoDTOS = userMapper.usersToUserInfoDTOs(users.getContent());

        var result = new PageImpl<>(userInfoDTOS, pageable, users.getTotalElements());
        return ResponseEntity.ok(result);
    }
}
