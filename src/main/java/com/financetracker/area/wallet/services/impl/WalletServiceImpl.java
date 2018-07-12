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

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper mapper;


    @Override
    public void createWallet(WalletBindingModel newWallet, Long userId) {
        Wallet wallet = new Wallet();

        if (checkIfWalletNameExists(newWallet.getName())) {
            throw new WalletNameAlreadyExists("Wallet name is already taken");
        }
        mapper.map(newWallet, wallet);
        wallet.setUserId(userId);

        walletRepository.save(wallet);
    }

    private boolean checkIfWalletNameExists(String name) {
        return walletRepository.findByName(name) != null;
    }
}
