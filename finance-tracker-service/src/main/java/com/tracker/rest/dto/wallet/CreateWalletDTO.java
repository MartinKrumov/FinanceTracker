package com.tracker.rest.dto.wallet;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CreateWalletDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z -.]+$", message = "Invalid Name")
    @Size(min = 4, max = 32)
    private String name;

    @NotNull
    private BigDecimal amount;
}
