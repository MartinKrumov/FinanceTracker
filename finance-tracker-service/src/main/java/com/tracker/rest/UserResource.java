package com.tracker.rest;

import com.tracker.domain.User;
import com.tracker.dto.user.UserInfoDTO;
import com.tracker.dto.user.UserLoginDTO;
import com.tracker.dto.user.UserRegisterDTO;
import com.tracker.mapper.UserMapper;
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
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final UserMapper userMapper;

    @ApiOperation(value = "Create user")
    @PostMapping("users/register")
    public ResponseEntity register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("Request for creating user has been received");
        User user = userMapper.convertToUser(userRegisterDTO);
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieve collection of users",
            notes = "Retrieve users for given page number, size and sort.")
    @GetMapping("/users")
    public ResponseEntity<Page<UserInfoDTO>> getUsers(Pageable pageable) {
        log.info("Request for retrieving page of users with pageNumber= {} and pageSize= {} has been received",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userService.findAll(pageable);
        List<UserInfoDTO> userInfoDTOS = userMapper.usersToUserInfoDTOs(users.getContent());

        var result = new PageImpl<>(userInfoDTOS, pageable, users.getTotalElements());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserLoginDTO> getUserByName(@PathVariable String username) {
        log.info("Request for getting user with email or username = [{}] has been received", username);

        UserLoginDTO userLoginDTO = userMapper.userToUserLoginDTO(userService.findByUsernameOrEmail(username));
        return ResponseEntity.ok(userLoginDTO);
    }
}
