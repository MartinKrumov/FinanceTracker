package com.tracker.rest.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private Boolean isRememberMe;
}
