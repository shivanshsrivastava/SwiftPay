package com.walletsystem.swiftpay.ledger.mapper;

import com.walletsystem.swiftpay.ledger.dto.LedgerEntryResponse;
import com.walletsystem.swiftpay.ledger.dto.LedgerHistoryResponse;
import com.walletsystem.swiftpay.ledger.entity.LedgerEntry;
import com.walletsystem.swiftpay.ledger.entity.LedgerEntryType;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class LedgerMapper {

    public LedgerHistoryResponse toHistoryResponse(
            LedgerEntry ledgerEntry,
            Wallet currentWallet
    ) {

        String counterpartyWalletNumber;

        if (ledgerEntry.getEntryType() == LedgerEntryType.DEBIT) {

            counterpartyWalletNumber =
                    ledgerEntry.getTransaction()
                            .getReceiverWallet()
                            .getWalletNumber();

        } else {

            counterpartyWalletNumber =
                    ledgerEntry.getTransaction()
                            .getSenderWallet()
                            .getWalletNumber();
        }

        return LedgerHistoryResponse.builder()
                .transactionReference(
                        ledgerEntry.getTransactionReference()
                )
                .entryType(
                        ledgerEntry.getEntryType()
                )
                .amount(
                        ledgerEntry.getAmount()
                )
                .balanceBefore(
                        ledgerEntry.getBalanceBefore()
                )
                .balanceAfter(
                        ledgerEntry.getBalanceAfter()
                )
                .counterpartyWalletNumber(
                        counterpartyWalletNumber
                )
                .createdAt(
                        ledgerEntry.getCreatedAt()
                )
                .build();
    }


    public LedgerEntryResponse toLedgerEntryResponse(
            LedgerEntry ledgerEntry
    ) {

        return LedgerEntryResponse.builder()
                .walletNumber(
                        ledgerEntry.getWallet().getWalletNumber()
                )
                .entryType(
                        ledgerEntry.getEntryType()
                )
                .amount(
                        ledgerEntry.getAmount()
                )
                .balanceBefore(
                        ledgerEntry.getBalanceBefore()
                )
                .balanceAfter(
                        ledgerEntry.getBalanceAfter()
                )
                .createdAt(
                        ledgerEntry.getCreatedAt()
                )
                .build();
    }

}