package com.financetracker.area.wallet.repository;


import com.financetracker.area.wallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByName(String name);
}
