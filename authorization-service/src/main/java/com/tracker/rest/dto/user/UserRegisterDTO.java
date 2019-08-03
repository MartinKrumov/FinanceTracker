package com.tracker.rest.dto.user;

import com.tracker.common.validatior.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class UserRegisterDTO {

    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    private String username;

    @ValidPassword
    @Size(min = 4, max = 32)
    private String password;

    @NotBlank
    @Size(min = 5, max = 64)
    @Pattern(regexp = "^(?:\\S+)@(?:\\S+)\\.(?:\\S+)$", message = "Invalid Email")
    private String email;

    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 32)
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    private String lastName;
}
