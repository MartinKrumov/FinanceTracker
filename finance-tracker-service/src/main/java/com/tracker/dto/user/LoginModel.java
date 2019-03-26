package com.tracker.dto.user;

import lombok.Data;

@Data
public class LoginModel {

    private String username;
    private String password;
    private Boolean isRememberMe;
}
