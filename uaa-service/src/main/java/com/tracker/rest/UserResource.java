package com.tracker.rest;

import com.tracker.domain.User;
import com.tracker.mapper.UserMapper;
import com.tracker.rest.dto.user.ResetPasswordDTO;
import com.tracker.rest.dto.user.UserInfoDTO;
import com.tracker.rest.dto.user.UserRegisterDTO;
import com.tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/users")
public class UserResource {

    private final UserMapper userMapper;
    private final UserService userService;

    public UserResource(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Operation(summary = "Register user",
            description = "Sends email with verification link.")
    @PostMapping("/register")
    public ResponseEntity<UserRegisterDTO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Creating user: {}", userRegisterDTO);
        User user = userService.register(userMapper.toUser(userRegisterDTO));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDtoIgnorePassword(user));
    }

    @Operation(summary =  "Complete user registration.",
            description = "Verifies users account.")
    @GetMapping("/complete-register")
    public ResponseEntity<Void> completeRegister(@RequestParam @NotBlank String token) {
        log.info("Completing registration with token: {}", token);
        userService.completeRegistration(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary =  "Initiates password reset for given user.",
            description = "Sends email with password reset link.")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam @Email String email) {
        log.info("Resetting the password for user: {}", email);
        userService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary =  "Check token validity.",
            description = "Validate if given token exist and it's not expired.")
    @GetMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@RequestParam @NotBlank String token) {
        log.info("Checking validity of token: {}", token);
        userService.validateToken(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary =  "Change password.",
            description = "Store new password if reset password token is valid and not expired.")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("Request for resetting password received for email: {}", resetPasswordDTO);
        userService.completePasswordReset(resetPasswordDTO.getToken(), resetPasswordDTO.getPassword());
        return ResponseEntity.ok().build();
    }

    @Operation(summary =  "Retrieve collection of users",
            description = "Retrieve users for given page number, size and sort.")
    @GetMapping
    public ResponseEntity<Page<UserInfoDTO>> getUsers(Pageable pageable) {
        log.info("Request for retrieving page of users with pageNumber= {} and pageSize= {} has been received",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userService.findAll(pageable);
        List<UserInfoDTO> userInfoDTOS = userMapper.usersToUserInfoDTOs(users.getContent());

        Page<UserInfoDTO> pageOfUserInfoDTO = new PageImpl<>(userInfoDTOS, pageable, users.getTotalElements());
        return ResponseEntity.ok(pageOfUserInfoDTO);
    }
}
