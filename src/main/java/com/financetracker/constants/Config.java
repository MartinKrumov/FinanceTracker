package com.financetracker.constants;

public final class Config {
    //Web Security Config
    public static final String FORM_LOGIN_PAGE = "/login";
    public static final String USERNAME_PARAMETER = "username";
    public static final String PasswordParameter = "password";
    public static final String AUTH_ENTRY_POINT = "/users/login";

    public static final String LOGOUT_URL = "/login?logout";

    private Config() {
    }
}
