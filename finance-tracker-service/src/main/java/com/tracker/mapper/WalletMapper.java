package com.tracker.mapper;

import com.tracker.domain.Wallet;
import com.tracker.dto.wallet.CreateWalletDTO;
import com.tracker.dto.wallet.WalletResponseModel;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet convertToWallet(CreateWalletDTO createWalletDTO);

    WalletResponseModel convertToCategoryResponseModel(Wallet category);

    Set<WalletResponseModel> convertToCategoryResponseModels(Set<Wallet> categories);
}
