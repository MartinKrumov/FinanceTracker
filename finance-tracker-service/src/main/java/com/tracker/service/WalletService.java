package com.tracker.service;

import com.tracker.domain.Wallet;
import com.tracker.dto.wallet.WalletInfoResponseDTO;

import java.util.Set;

public interface WalletService {

    Wallet save(Wallet wallet);

    Wallet findByIdOrThrow(Long walletId);

    void createWallet(Wallet wallet, Long userId);

    Set<Wallet> findAllByUserId(Long userId);

    WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId);
}
