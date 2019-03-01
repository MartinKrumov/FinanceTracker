package com.tracker.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRegistrationModel {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    @Size(min = 4, max = 32)
    private String username;

    @NotBlank
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Invalid Password")
    @Size(min = 4, max = 32)
    private String password;

    @Pattern(regexp = "^(?:\\S+)@(?:\\S+)\\.(?:\\S+)$", message = "Invalid Email")
    @Size(min = 5, max = 64)
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    @Size(min = 4, max = 32)
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    @Size(min = 4, max = 32)
    private String lastName;
}
