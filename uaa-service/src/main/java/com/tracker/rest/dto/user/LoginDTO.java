package com.tracker.rest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
