package com.tracker.area.user.model;

import lombok.Data;

@Data
public class LoginModel {

    private String username;

    private String password;

    private Boolean isRememberMe;
}
