package com.tracker.dto.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class UserLoginDTO implements Serializable {

    private String username;
    private String email;
    private String password;
    private Set<String> authorities;
}
