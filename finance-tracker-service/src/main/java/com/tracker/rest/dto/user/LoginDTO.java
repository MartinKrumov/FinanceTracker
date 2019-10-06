package com.tracker.rest.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private Boolean isRememberMe;
}
