package com.financetracker.area.wallet.services;

import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.models.WalletInfoResponseDTO;
import com.financetracker.area.wallet.models.WalletResponseModel;

import java.util.List;

public interface WalletService {
    void createWallet(WalletBindingModel walletModel, Long userId);

    List<WalletResponseModel> findAllByUserId(Long userId);

    WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId);
}
