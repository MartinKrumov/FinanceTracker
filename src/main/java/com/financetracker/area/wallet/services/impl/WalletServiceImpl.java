package com.financetracker.area.wallet.services.impl;

import com.financetracker.area.wallet.domain.Wallet;
import com.financetracker.area.wallet.exceptions.WalletNameAlreadyExists;
import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.repository.WalletRepository;
import com.financetracker.area.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper mapper;


    @Override
    @Transactional
    public void createWallet(WalletBindingModel newWallet, Long userId) {
        if (checkIfWalletNameExists(newWallet.getName())) {
            throw new WalletNameAlreadyExists("Wallet name is already taken");
        }

        Wallet wallet = mapper.map(newWallet, Wallet.class);
        wallet.setUserId(userId);

        walletRepository.save(wallet);
    }

    private boolean checkIfWalletNameExists(String name) {
        return walletRepository.findByName(name) != null;
    }
}
