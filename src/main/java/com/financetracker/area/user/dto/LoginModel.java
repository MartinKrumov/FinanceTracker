package com.financetracker.area.user.dto;

import lombok.Data;

@Data
public class LoginModel {

    private String username;

    private String password;

    private Boolean isRememberMe;
}
