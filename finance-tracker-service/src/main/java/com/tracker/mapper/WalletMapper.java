package com.tracker.mapper;

import com.tracker.domain.Wallet;
import com.tracker.rest.dto.wallet.CreateWalletDTO;
import com.tracker.rest.dto.wallet.WalletDetailsDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet convertToWallet(CreateWalletDTO createWalletDTO);

    WalletDetailsDTO convertToCategoryResponseModel(Wallet category);

    Set<WalletDetailsDTO> convertToCategoryResponseModels(Set<Wallet> categories);
}
