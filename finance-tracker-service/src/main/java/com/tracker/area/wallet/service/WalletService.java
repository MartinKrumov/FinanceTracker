package com.tracker.area.wallet.service;

import com.tracker.area.wallet.domain.Wallet;
import com.tracker.area.wallet.model.WalletBindingModel;
import com.tracker.area.wallet.model.WalletInfoResponseDTO;
import com.tracker.area.wallet.model.WalletResponseModel;

import java.util.List;

public interface WalletService {

    Wallet save(Wallet wallet);

    void createWallet(WalletBindingModel walletModel, Long userId);

    List<WalletResponseModel> findAllByUserId(Long userId);

    WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId);
}
