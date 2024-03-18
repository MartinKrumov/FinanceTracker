package com.tracker.rest.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    @Size(min = 4, max = 32)
    private String username;

    @NotBlank
//    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}", message = "Invalid Password")
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
