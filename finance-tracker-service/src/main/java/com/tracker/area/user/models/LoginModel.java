package com.tracker.area.user.models;

import lombok.Data;

@Data
public class LoginModel {

    private String username;

    private String password;

    private Boolean isRememberMe;
}
