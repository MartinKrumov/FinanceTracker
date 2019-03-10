package com.tracker.dto.wallet;

import com.tracker.dto.budget.BudgetResponseModel;
import com.tracker.dto.transaction.TransactionResponseDTO;
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
public class WalletInfoResponseDTO implements Serializable {

    private Long id;
    private String name;
    private BigDecimal amount;
    private BigDecimal initialAmount;
    private List<TransactionResponseDTO> transactions;
    private List<BudgetResponseModel> budgets;
}
