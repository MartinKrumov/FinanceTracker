package com.tracker.rest;

import com.tracker.domain.User;
import com.tracker.mapper.UserMapper;
import com.tracker.rest.dto.user.ResetPasswordDTO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserMapper userMapper;
    private final UserService userService;

    @ApiOperation(value = "Register user",
            notes = "Sends email with verification link.")
    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Creating user: {}", userRegisterDTO);
        User user = userService.register(userMapper.toUser(userRegisterDTO));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDtoIgnorePassword(user));
    }

    @ApiOperation(value = "Complete user registration.",
            notes = "Verifies users account.")
    @GetMapping("/complete-register")
    public ResponseEntity completeRegister(@RequestParam("token") @NotBlank String token) {
        log.info("Completing registration with token: {}", token);
        userService.completeRegistration(token);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Initiates password reset for given user.",
            notes = "Sends email with password reset link.")
    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@RequestParam("email") @Email String email) {
        log.info("Resetting the password for user: {}", email);
        userService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Check token validity.",
            notes = "Validate if given token exist and it's not expired.")
    @GetMapping("/validate-token")
    public ResponseEntity validateToken(@RequestParam("token") @NotBlank String token) {
        log.info("Checking validity of token: {}", token);
        userService.validateToken(token);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change password.",
            notes = "Store new password if reset password token is valid and not expired.")
    @PostMapping("/change-password")
    public ResponseEntity changePassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("Request for resetting password received for email: {}", resetPasswordDTO);
        userService.completePasswordReset(resetPasswordDTO.getToken(), resetPasswordDTO.getPassword());
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

        Page<UserInfoDTO> pageOfUserInfoDTO = new PageImpl<>(userInfoDTOS, pageable, users.getTotalElements());
        return ResponseEntity.ok(pageOfUserInfoDTO);
    }
}
