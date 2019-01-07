package com.financetracker.area.wallet.repository;


import com.financetracker.area.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByName(String name);

    List<Wallet> findAllByUser(User user);

    Optional<Wallet> findByIdAndUser(Long walletId, User user);
}
