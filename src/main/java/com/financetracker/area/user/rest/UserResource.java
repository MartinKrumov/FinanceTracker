package com.financetracker.area.user.rest;

import com.financetracker.area.user.models.UserRegistrationModel;
import com.financetracker.area.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserResource {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody UserRegistrationModel newUser){
        userService.register(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}