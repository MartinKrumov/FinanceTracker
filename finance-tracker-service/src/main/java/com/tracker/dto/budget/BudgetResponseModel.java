package com.tracker.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Martin Krumov
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResponseModel implements Serializable {

    private Long id;
    private String name;
    private BigDecimal initialAmount;
    private BigDecimal amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
