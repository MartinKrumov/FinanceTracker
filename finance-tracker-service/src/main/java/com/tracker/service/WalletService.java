package com.tracker.service;

import com.tracker.domain.Wallet;
import com.tracker.dto.wallet.WalletBindingModel;
import com.tracker.dto.wallet.WalletInfoResponseDTO;
import com.tracker.dto.wallet.WalletResponseModel;

import java.util.List;

public interface WalletService {

    Wallet save(Wallet wallet);

    Wallet findByIdOrThrow(Long walletId);

    void createWallet(WalletBindingModel walletModel, Long userId);

    List<WalletResponseModel> findAllByUserId(Long userId);

    WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId);
}
