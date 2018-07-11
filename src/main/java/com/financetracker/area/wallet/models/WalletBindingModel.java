package com.financetracker.area.wallet.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBindingModel {

    private String name;

    private BigDecimal amount;
}
