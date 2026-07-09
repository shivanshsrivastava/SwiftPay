package com.walletsystem.swiftpay.ledger.service.impl;

import com.walletsystem.swiftpay.common.exception.TransactionNotFoundException;
import com.walletsystem.swiftpay.common.service.CurrentUserService;
import com.walletsystem.swiftpay.ledger.dto.LedgerEntryResponse;
import com.walletsystem.swiftpay.ledger.dto.LedgerHistoryResponse;
import com.walletsystem.swiftpay.ledger.entity.LedgerEntry;
import com.walletsystem.swiftpay.ledger.entity.LedgerEntryType;
import com.walletsystem.swiftpay.ledger.mapper.LedgerMapper;
import com.walletsystem.swiftpay.ledger.model.BalanceUpdateResult;
import com.walletsystem.swiftpay.ledger.repository.LedgerRepository;
import com.walletsystem.swiftpay.ledger.service.LedgerService;
import com.walletsystem.swiftpay.transaction.entity.Transaction;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerServiceImpl
        implements LedgerService {

    private final LedgerRepository ledgerRepository;
    private final CurrentUserService currentUserService;
    private final LedgerMapper ledgerMapper;

    @Override
    public void recordTransferEntries(
            Transaction transaction,
            Wallet senderWallet,
            Wallet receiverWallet,
            BalanceUpdateResult balanceUpdateResult
    ) {
        System.out.println("========== INSIDE ledger service() ==========");

        LedgerEntry debitEntry = buildLedgerEntry(
                transaction,
                senderWallet,
                LedgerEntryType.DEBIT,
                balanceUpdateResult.getSenderBalanceBefore(),
                balanceUpdateResult.getSenderBalanceAfter()
        );

        LedgerEntry creditEntry = buildLedgerEntry(
                transaction,
                receiverWallet,
                LedgerEntryType.CREDIT,
                balanceUpdateResult.getReceiverBalanceBefore(),
                balanceUpdateResult.getReceiverBalanceAfter()
        );

        ledgerRepository.saveAll(
                List.of(debitEntry, creditEntry)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LedgerHistoryResponse> getMyLedgerHistory() {
        Wallet currentWallet =
                currentUserService.getCurrentUserWallet();

        List<LedgerEntry> ledgerEntries =
                ledgerRepository.findByWalletIdOrderByCreatedAtDesc(
                        currentWallet.getId()
                );

//        return ledgerEntries.stream()
//                .map(ledgerMapper::toHistoryResponse)
//                .toList();

        return ledgerEntries.stream()
                .map(entry ->
                        ledgerMapper.toHistoryResponse(
                                entry,
                                currentWallet
                        ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LedgerEntryResponse> getLedgerEntriesByTransaction(
            String transactionReference
    ) {

        List<LedgerEntry> ledgerEntries =
                ledgerRepository
                        .findByTransactionReferenceOrderByCreatedAtAsc(
                                transactionReference
                        );

        Wallet currentWallet =
                currentUserService.getCurrentUserWallet();

        boolean authorized = ledgerEntries.stream()
                .anyMatch(entry ->
                        entry.getWallet().getId()
                                .equals(currentWallet.getId())
                );

        if (!authorized) {
            throw new AccessDeniedException(
                    "You are not authorized to view these ledger entries."
            );
        }

        if (ledgerEntries.isEmpty()) {
            throw new TransactionNotFoundException(
                    "Transaction not found."
            );
        }

        return ledgerEntries.stream()
                .map(ledgerMapper::toLedgerEntryResponse)
                .toList();
    }

    private LedgerEntry buildLedgerEntry(

            Transaction transaction,

            Wallet wallet,

            LedgerEntryType entryType,

            BigDecimal balanceBefore,

            BigDecimal balanceAfter
    ) {

        return LedgerEntry.builder()
                .transaction(transaction)
                .transactionReference(
                        transaction.getTransactionReference()
                )
                .wallet(wallet)
                .entryType(entryType)
                .amount(transaction.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .createdAt(transaction.getCreatedAt())
                .build();

    }


}