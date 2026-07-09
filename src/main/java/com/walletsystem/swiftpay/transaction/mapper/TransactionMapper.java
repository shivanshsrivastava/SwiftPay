package com.walletsystem.swiftpay.transaction.mapper;

import com.walletsystem.swiftpay.transaction.dto.response.TransactionDetailsResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionHistoryResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransferResponse;
import com.walletsystem.swiftpay.transaction.entity.Transaction;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionHistoryResponse toHistoryResponse(
            Transaction transaction,
            Wallet currentWallet
    ) {

        String type = transaction.getSenderWallet()
                .getId()
                .equals(currentWallet.getId())
                ? "DEBIT"
                : "CREDIT";

        return TransactionHistoryResponse.builder()
                .transactionReference(transaction.getTransactionReference())
                .senderWalletNumber(
                        transaction.getSenderWallet().getWalletNumber()
                )
                .receiverWalletNumber(
                        transaction.getReceiverWallet().getWalletNumber()
                )
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .type(type)
                .build();
    }

    public TransferResponse toTransferResponse(
            Transaction transaction
    ) {

        return TransferResponse.builder()
                .transactionReference(transaction.getTransactionReference())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .build();
    }


    public TransactionDetailsResponse
    toDetailsResponse(Transaction transaction) {

        return TransactionDetailsResponse.builder()
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .senderWalletNumber(
                        transaction.getSenderWallet()
                                .getWalletNumber()
                )
                .receiverWalletNumber(
                        transaction.getReceiverWallet()
                                .getWalletNumber()
                )
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}