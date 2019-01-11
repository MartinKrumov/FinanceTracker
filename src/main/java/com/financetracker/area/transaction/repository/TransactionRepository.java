package com.financetracker.area.transaction.repository;

import com.financetracker.area.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Martin Krumov
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByWalletId(Long walletId);
}
