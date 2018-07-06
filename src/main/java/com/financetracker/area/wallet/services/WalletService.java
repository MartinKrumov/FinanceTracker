package com.financetracker.area.wallet.services;

import com.financetracker.area.wallet.dto.WalletBindingModel;

public interface WalletService {
    void createWallet(WalletBindingModel newWallet, Long userId);
}
