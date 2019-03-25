package com.tracker.service.impl;

import com.tracker.common.exception.WalletNameAlreadyExists;
import com.tracker.domain.Transaction;
import com.tracker.domain.User;
import com.tracker.domain.Wallet;
import com.tracker.domain.enums.TransactionType;
import com.tracker.dto.budget.BudgetResponseModel;
import com.tracker.dto.transaction.TransactionResponseDTO;
import com.tracker.dto.wallet.WalletBindingModel;
import com.tracker.dto.wallet.WalletInfoResponseDTO;
import com.tracker.dto.wallet.WalletResponseModel;
import com.tracker.repository.WalletRepository;
import com.tracker.service.UserService;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletServiceImpl implements WalletService {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final WalletRepository walletRepository;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByIdOrThrow(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow();
    }

    @Override
    @Transactional
    public void createWallet(WalletBindingModel walletModel, Long userId) {
        User user = userService.findByIdOrThrow(userId);
        validateForUniqueWalletName(walletModel, user.getWallets());

        Wallet wallet = modelMapper.map(walletModel, Wallet.class);
        wallet.setInitialAmount(wallet.getAmount());

        user.addWallet(wallet);

        userService.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WalletResponseModel> findAllByUserId(Long userId) {
        User user = userService.findByIdOrThrow(userId);
        Type listType = new TypeToken<List<WalletResponseModel>>() {}.getType();
        return modelMapper.map(user.getWallets(), listType);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId) {
        User user = userService.findByIdOrThrow(userId);

        Wallet wallet = user.getWallets().stream()
                .filter(w -> Objects.equals(w.getId(), walletId))
                .findFirst()
                .orElseThrow();

        return getWalletInfoResponseDTO(wallet);
    }

    @Override
    public void adjustWalletAmount(Wallet wallet, Transaction transaction) {
        if (TransactionType.EXPENSE.equals(transaction.getType())) {
            wallet.setAmount(wallet.getAmount().subtract(transaction.getAmount()));
        } else {
            wallet.setAmount(wallet.getAmount().add(transaction.getAmount()));
        }
    }

    private void validateForUniqueWalletName(WalletBindingModel walletModel, Set<Wallet> wallets) {
        boolean isWalletNameUnique = wallets.stream()
                .map(Wallet::getName)
                .anyMatch(name -> equalsAnyIgnoreCase(name, walletModel.getName()));

        if (isWalletNameUnique) {
            throw new WalletNameAlreadyExists("Wallet name is already exists.");
        }
    }

    private WalletInfoResponseDTO getWalletInfoResponseDTO(Wallet wallet) {
        var listType = new TypeToken<List<TransactionResponseDTO>>() {}.getType();
        List<TransactionResponseDTO> transactionResponseDTOs = modelMapper.map(wallet.getTransactions(), listType);

        listType = new TypeToken<List<BudgetResponseModel>>() {}.getType();
        List<BudgetResponseModel> budgetsDTOs = modelMapper.map(wallet.getBudgets(), listType);

        return WalletInfoResponseDTO.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .amount(wallet.getAmount())
                .initialAmount(wallet.getInitialAmount())
                .budgets(budgetsDTOs)
                .transactions(transactionResponseDTOs)
                .build();
    }
}
