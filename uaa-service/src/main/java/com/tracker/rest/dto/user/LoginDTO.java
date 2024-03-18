package com.tracker.rest.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(exclude = "password")
public class LoginDTO {

    @NotBlank
    @Size(min = 5, max = 64)
    private String username;
    @NotBlank
    private String password;

    private Boolean isRememberMe;
}
