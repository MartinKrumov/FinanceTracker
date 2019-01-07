package com.financetracker.area.wallet.models;

import com.financetracker.area.transaction.model.TransactionResponseDTO;
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
    private List<TransactionResponseDTO> transactionDTO;
}
