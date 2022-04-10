package com.tracker.mapper;

import com.tracker.domain.Transaction;
import com.tracker.rest.dto.transaction.TransactionCreationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toTransaction(TransactionCreationDTO transactionCreationDTO);
}
