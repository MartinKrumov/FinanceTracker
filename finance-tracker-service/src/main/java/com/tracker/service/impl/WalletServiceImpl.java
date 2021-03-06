package com.tracker.service.impl;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.domain.User;
import com.tracker.domain.Wallet;
import com.tracker.rest.dto.budget.BudgetInfoDTO;
import com.tracker.rest.dto.transaction.TransactionDetailsDTO;
import com.tracker.rest.dto.wallet.WalletInfoDTO;
import com.tracker.repository.WalletRepository;
import com.tracker.service.UserService;
import com.tracker.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

@Service
@RequiredArgsConstructor
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
    public void createWallet(Wallet wallet, Long userId) {
        User user = userService.findByIdOrThrow(userId);
        validateForUniqueWalletName(wallet.getName(), user.getWallets());

        wallet.setInitialAmount(wallet.getAmount());

        user.addWallet(wallet);

        userService.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Wallet> findAllByUserId(Long userId) {
        Set<Wallet> wallets = userService.findByIdOrThrow(userId).getWallets();
        wallets.size();
        return wallets;
    }

    @Override
    @Transactional(readOnly = true)
    public WalletInfoDTO findByIdAndUser(Long userId, Long walletId) {
        User user = userService.findByIdOrThrow(userId);

        Wallet wallet = user.getWallets().stream()
                .filter(w -> Objects.equals(w.getId(), walletId))
                .findFirst()
                .orElseThrow();

        return getWalletInfoResponseDTO(wallet);
    }

    private void validateForUniqueWalletName(String walletName, Set<Wallet> wallets) {
        boolean isWalletNameUnique = wallets.stream()
                .map(Wallet::getName)
                .anyMatch(name -> equalsAnyIgnoreCase(name, walletName));

        if (isWalletNameUnique) {
            throw new EntityAlreadyExistException("Wallet name is already exists.");
        }
    }

    private WalletInfoDTO getWalletInfoResponseDTO(Wallet wallet) {
        var listType = new TypeToken<List<TransactionDetailsDTO>>() {}.getType();
        List<TransactionDetailsDTO> transactionDetailsDTOs = modelMapper.map(wallet.getTransactions(), listType);

        listType = new TypeToken<List<BudgetInfoDTO>>() {}.getType();
        List<BudgetInfoDTO> budgetsDTOs = modelMapper.map(wallet.getBudgets(), listType);

        return WalletInfoDTO.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .amount(wallet.getAmount())
                .initialAmount(wallet.getInitialAmount())
                .budgets(budgetsDTOs)
                .transactions(transactionDetailsDTOs)
                .build();
    }
}
