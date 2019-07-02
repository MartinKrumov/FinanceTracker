package com.tracker.rest.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Martin Krumov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetCreationDTO implements Serializable {

    private String name;
    private BigDecimal amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Long categoryId;
}
