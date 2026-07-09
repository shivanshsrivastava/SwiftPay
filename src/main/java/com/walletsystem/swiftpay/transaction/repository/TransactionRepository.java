package com.walletsystem.swiftpay.transaction.repository;

import com.walletsystem.swiftpay.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionReference(
            String transactionReference
    );

    List<Transaction> findBySenderWalletIdOrReceiverWalletId(
            Long senderWalletId,
            Long receiverWalletId
    );
}