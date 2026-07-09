package com.walletsystem.swiftpay.transaction.service;

import com.walletsystem.swiftpay.transaction.dto.request.TransferRequest;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionDetailsResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionHistoryResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransferResponse;

import java.util.List;

public interface TransactionService {
    TransferResponse transfer(
            TransferRequest request
    );

    List<TransactionHistoryResponse> getMyTransactions();

    TransactionDetailsResponse getTransactionDetails(
            String transactionReference
    );
}
