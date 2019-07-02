package com.tracker.rest.dto.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class UserDetailsDTO implements Serializable {

    private String username;
    private String email;
    private String password;
    private Set<String> authorities;
}
