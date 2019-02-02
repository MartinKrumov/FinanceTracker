package com.tracker.area.wallet.services;

import com.tracker.area.wallet.domain.Wallet;
import com.tracker.area.wallet.models.WalletBindingModel;
import com.tracker.area.wallet.models.WalletInfoResponseDTO;
import com.tracker.area.wallet.models.WalletResponseModel;

import java.util.List;

public interface WalletService {

    Wallet save(Wallet wallet);

    void createWallet(WalletBindingModel walletModel, Long userId);

    List<WalletResponseModel> findAllByUserId(Long userId);

    WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId);
}
