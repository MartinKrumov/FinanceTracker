package com.tracker.rest.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
