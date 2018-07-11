package com.financetracker.area.wallet.services;

import com.financetracker.area.wallet.models.WalletBindingModel;

public interface WalletService {
    void createWallet(WalletBindingModel newWallet, Long userId);
}
