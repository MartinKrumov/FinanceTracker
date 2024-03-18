package com.tracker.rest.dto.transaction;

import com.tracker.domain.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreationDTO {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private TransactionType type;
    @NotNull
    private LocalDateTime date;
    private String description;
    @NotNull
    private Long categoryId;
}
