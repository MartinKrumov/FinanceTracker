package com.tracker.rest.dto.user;

import com.tracker.common.validator.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

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
