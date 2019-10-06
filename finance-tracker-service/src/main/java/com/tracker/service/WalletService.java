package com.tracker.service;

import com.tracker.domain.Wallet;
import com.tracker.rest.dto.wallet.WalletInfoDTO;

import java.util.Set;

public interface WalletService {

    /**
     * Save wallet.
     *
     * @param wallet the wallet
     * @return the wallet
     */
    Wallet save(Wallet wallet);

    /**
     * Find by id or throw exception if not found.
     *
     * @param walletId the wallet id
     * @return the wallet
     */
    Wallet findByIdOrThrow(Long walletId);

    /**
     * Create wallet.
     *
     * @param wallet the wallet
     * @param userId the user id
     */
    void createWallet(Wallet wallet, Long userId);

    /**
     * Find all by userId.
     *
     * @param userId the user id
     * @return Set of {@link Wallet}
     */
    Set<Wallet> findAllByUserId(Long userId);

    /**
     * Find by id and user.
     *
     * @param userId   the user id
     * @param walletId the wallet id
     * @return the wallet info dto
     */
    WalletInfoDTO findByIdAndUser(Long userId, Long walletId);
}
