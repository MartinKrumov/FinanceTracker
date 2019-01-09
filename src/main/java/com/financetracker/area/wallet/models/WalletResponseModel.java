package com.financetracker.area.wallet.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponseModel {

    private Long id;
    private String name;
    private BigDecimal amount;
    private BigDecimal initialAmount;
}
