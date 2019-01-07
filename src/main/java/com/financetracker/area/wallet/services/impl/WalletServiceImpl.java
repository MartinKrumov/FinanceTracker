package com.financetracker.area.wallet.services.impl;

import com.financetracker.area.user.repositories.UserRepository;
import com.financetracker.area.wallet.domain.Wallet;
import com.financetracker.area.wallet.exceptions.WalletNameAlreadyExists;
import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.repository.WalletRepository;
import com.financetracker.area.wallet.services.WalletService;
import com.financetracker.enums.CustomEntity;
import com.financetracker.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void createWallet(WalletBindingModel newWallet, Long userId) {
        walletRepository.findByName(newWallet.getName())
                .orElseThrow(() -> new WalletNameAlreadyExists("Wallet name is already taken"));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(CustomEntity.USER));

        var wallet = modelMapper.map(newWallet, Wallet.class);
        wallet.setUser(user);

        walletRepository.save(wallet);
    }

    @Override
    public List<WalletResponseModel> findAllByUserId(Long userId) {
        var user = userService.findOneOrThrow(userId);
        List<Wallet> walletsForUser = walletRepository.findAllByUser(user);

        Type listType = new TypeToken<List<WalletResponseModel>>() {}.getType();
        return modelMapper.map(walletsForUser, listType);
    }

    @Override
    @Transactional
    public WalletInfoResponseDTO findByIdAndUser(Long walletId, Long userId) {
        var user = userService.findOneOrThrow(userId);
        var wallet = walletRepository.findByIdAndUser(walletId, user)
                .orElseThrow(() -> new EntityNotFoundException(CustomEntity.WALLET));

        return getWalletInfoResponseDTO(wallet);
    }

    private WalletInfoResponseDTO getWalletInfoResponseDTO(Wallet wallet) {
        List<TransactionResponseDTO> transactionResponseDTOS = wallet.getTransactions().stream()
                .map(t -> TransactionResponseDTO.builder()
                        .id(t.getId())
                        .type(t.getType())
                        .amount(t.getAmount())
                        .date(t.getDate())
                        .categoryName(t.getCategory().getName())
                        .build())
                .collect(Collectors.toList());

        return WalletInfoResponseDTO.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .amount(wallet.getAmount())
                .initialAmount(wallet.getInitialAmount())
                .transactionDTO(transactionResponseDTOS)
                .build();
    }
}
