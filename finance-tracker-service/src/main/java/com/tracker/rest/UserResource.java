package com.tracker.rest;

import com.tracker.domain.User;
import com.tracker.mapper.UserMapper;
import com.tracker.rest.dto.user.UserDetailsDTO;
import com.tracker.rest.dto.user.UserInfoDTO;
import com.tracker.rest.dto.user.UserRegisterDTO;
import com.tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create user")
    @PostMapping("users/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Request for creating user has been received");
        User user = userMapper.convertToUser(userRegisterDTO);
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve collection of users",
            description = "Retrieve users for given page number, size and sort.")
    @GetMapping("/users")
    public ResponseEntity<Page<UserInfoDTO>> getUsers(@ParameterObject Pageable pageable) {
        log.info("Request for retrieving page of users with pageNumber= {} and pageSize= {} has been received",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<UserInfoDTO> users = userService.findAll(pageable)
                .map(userMapper::userToUserInfoDTO);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDetailsDTO> getUserByName(@PathVariable String username) {
        log.info("Request for getting user with email or username = [{}] has been received", username);

        UserDetailsDTO userDetailsDTO = userMapper.userToUserLoginDTO(userService.findByUsernameOrEmail(username));
        return ResponseEntity.ok(userDetailsDTO);
    }
}
