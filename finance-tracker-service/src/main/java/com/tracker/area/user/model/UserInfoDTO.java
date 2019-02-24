package com.tracker.area.user.model;

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
