package com.tracker.rest.dto.user;

import com.tracker.common.validator.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class ResetPasswordDTO {

    @NotBlank
    @Size(min = 20, max = 64)
    private String token;

    @ValidPassword
    private String password;
}
