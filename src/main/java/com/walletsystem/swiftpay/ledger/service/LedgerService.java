package com.walletsystem.swiftpay.ledger.service;

import com.walletsystem.swiftpay.ledger.dto.LedgerEntryResponse;
import com.walletsystem.swiftpay.ledger.dto.LedgerHistoryResponse;
import com.walletsystem.swiftpay.ledger.model.BalanceUpdateResult;
import com.walletsystem.swiftpay.transaction.entity.Transaction;
import com.walletsystem.swiftpay.wallet.entity.Wallet;

import java.util.List;

public interface LedgerService {

     void recordTransferEntries(
            Transaction transaction,
            Wallet senderWallet,
            Wallet receiverWallet,
            BalanceUpdateResult balanceUpdateResult
    );

    List<LedgerHistoryResponse> getMyLedgerHistory();

    List<LedgerEntryResponse> getLedgerEntriesByTransaction(
            String transactionReference
    );



}
