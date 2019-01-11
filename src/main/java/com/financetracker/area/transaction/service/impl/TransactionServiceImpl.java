package com.financetracker.area.transaction.service.impl;

import com.financetracker.area.transaction.domain.Transaction;
import com.financetracker.area.transaction.repository.TransactionRepository;
import com.financetracker.area.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Martin Krumov
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAllByWalletId(Long walletId) {
        return transactionRepository.findAllByWalletId(walletId);
    }
}
