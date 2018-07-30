package com.financetracker.area.wallet.services.impl;

import com.financetracker.area.user.domain.User;
import com.financetracker.area.user.exceptions.UserNotFoundException;
import com.financetracker.area.user.repositories.UserRepository;
import com.financetracker.area.wallet.domain.Wallet;
import com.financetracker.area.wallet.exceptions.WalletNameAlreadyExists;
import com.financetracker.area.wallet.models.WalletBindingModel;
import com.financetracker.area.wallet.repository.WalletRepository;
import com.financetracker.area.wallet.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.ws.http.HTTPException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createWallet(WalletBindingModel newWallet, Long userId) {
        if (checkIfWalletNameExists(newWallet.getName())) {
            throw new WalletNameAlreadyExists("Wallet name is already taken");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = " + userId + "does not exist."));

        Wallet wallet = mapper.map(newWallet, Wallet.class);
        wallet.setUser(user);

        walletRepository.save(wallet);
    }

    private boolean checkIfWalletNameExists(String name) {
        return walletRepository.findByName(name) != null;
    }
}
