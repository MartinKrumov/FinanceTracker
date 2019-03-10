package com.tracker.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Martin Krumov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetRequestModel {

    private String name;
    private BigDecimal amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Long categoryId;
}
