package com.tracker.area.transaction.model;

import com.tracker.area.transaction.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO implements Serializable {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private String categoryName;
}
