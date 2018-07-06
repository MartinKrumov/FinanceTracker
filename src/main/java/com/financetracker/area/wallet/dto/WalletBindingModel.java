package com.financetracker.area.wallet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBindingModel {

    private String name;

    private BigDecimal amount;
}
