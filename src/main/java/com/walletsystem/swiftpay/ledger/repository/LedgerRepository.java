package com.walletsystem.swiftpay.ledger.repository;

import com.walletsystem.swiftpay.ledger.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByWalletIdOrderByCreatedAtDesc(
            Long walletId
    );

    List<LedgerEntry> findByTransactionReferenceOrderByCreatedAtAsc(
            String transactionReference
    );
}