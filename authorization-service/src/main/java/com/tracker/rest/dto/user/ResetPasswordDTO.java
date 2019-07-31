package com.tracker.rest.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class ResetPasswordDTO {

    @NotBlank
    @Size(min = 20, max = 64)
    private String token;

    @NotBlank
//    @ValidPassword TODO: Password validations
    private String password;
}
