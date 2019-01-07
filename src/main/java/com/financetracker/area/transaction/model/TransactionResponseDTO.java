package com.financetracker.area.transaction.model;

import com.financetracker.area.transaction.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDTO implements Serializable {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private String categoryName;
}
