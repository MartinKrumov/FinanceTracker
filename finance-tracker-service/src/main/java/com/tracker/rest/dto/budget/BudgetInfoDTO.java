package com.tracker.rest.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetInfoDTO implements Serializable {

    private Long id;
    private String name;
    private BigDecimal initialAmount;
    private BigDecimal amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
