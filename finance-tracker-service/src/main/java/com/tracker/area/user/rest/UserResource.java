package com.tracker.area.user.rest;

import com.tracker.area.user.models.UserRegistrationModel;
import com.tracker.area.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResource {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody UserRegistrationModel newUser) {
        log.info("Request for creating user has been received");
        userService.register(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
