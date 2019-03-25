package com.tracker.mapper;

import com.tracker.domain.Transaction;
import com.tracker.dto.transaction.TransactionRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction convertToTransaction(TransactionRequest transactionRequest);
}
