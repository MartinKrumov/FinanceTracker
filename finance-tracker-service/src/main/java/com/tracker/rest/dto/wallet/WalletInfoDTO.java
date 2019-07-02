package com.tracker.rest.dto.wallet;

import com.tracker.rest.dto.budget.BudgetInfoDTO;
import com.tracker.rest.dto.transaction.TransactionDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletInfoDTO implements Serializable {

    private Long id;
    private String name;
    private BigDecimal amount;
    private BigDecimal initialAmount;
    private List<TransactionDetailsDTO> transactions;
    private List<BudgetInfoDTO> budgets;
}
