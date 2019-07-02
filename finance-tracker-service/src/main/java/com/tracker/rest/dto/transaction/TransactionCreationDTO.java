package com.tracker.rest.dto.transaction;

import com.tracker.domain.enums.TransactionType;
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

    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime date;
    private String description;
    private Long categoryId;
}
