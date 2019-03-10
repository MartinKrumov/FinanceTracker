package com.tracker.service.impl;

import com.tracker.dto.budget.BudgetResponseModel;
import com.tracker.dto.transaction.TransactionResponseDTO;
import com.tracker.domain.User;
import com.tracker.service.UserService;
import com.tracker.domain.Wallet;
import com.tracker.common.exception.WalletNameAlreadyExists;
import com.tracker.dto.wallet.WalletBindingModel;
import com.tracker.dto.wallet.WalletInfoResponseDTO;
import com.tracker.dto.wallet.WalletResponseModel;
import com.tracker.repository.WalletRepository;
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

import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletServiceImpl implements WalletService {

    private final UserService userService;
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional
    public void createWallet(WalletBindingModel walletModel, Long userId) {
        var user = userService.findOneOrThrow(userId);
        validateForUniqueWalletName(walletModel, user);

        var wallet = modelMapper.map(walletModel, Wallet.class);
        wallet.setUserId(userId);
        wallet.setInitialAmount(wallet.getAmount());
        user.addWallet(wallet);

        userService.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WalletResponseModel> findAllByUserId(Long userId) {
        var user = userService.findOneOrThrow(userId);
        Type listType = new TypeToken<List<WalletResponseModel>>() {}.getType();
        return modelMapper.map(user.getWallets(), listType);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletInfoResponseDTO findByIdAndUser(Long userId, Long walletId) {
        var user = userService.findOneOrThrow(userId);

        var wallet = user.getWallets().stream()
                .filter(w -> Objects.equals(w.getId(), walletId))
                .findFirst()
                .orElseThrow();

        return getWalletInfoResponseDTO(wallet);
    }

    private void validateForUniqueWalletName(WalletBindingModel walletModel, User user) {
        boolean isWalletNameUnique = user.getWallets().stream()
                .map(Wallet::getName)
                .anyMatch(name -> equalsAnyIgnoreCase(name, walletModel.getName()));

        if (isWalletNameUnique) {
            throw new WalletNameAlreadyExists("Wallet name is already taken");
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
