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
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createWallet(WalletBindingModel newWallet, Long userId) {
        walletRepository.findByName(newWallet.getName())
                .orElseThrow(() -> new WalletNameAlreadyExists("Wallet name is already taken"));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(CustomEntity.USER));

        var wallet = mapper.map(newWallet, Wallet.class);
        wallet.setUser(user);

        walletRepository.save(wallet);
    }
}
