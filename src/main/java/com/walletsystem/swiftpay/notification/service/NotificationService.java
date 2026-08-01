package com.walletsystem.swiftpay.notification.service;

import com.walletsystem.swiftpay.transaction.entity.Transaction;
import com.walletsystem.swiftpay.wallet.entity.Wallet;

public interface NotificationService {
    void sendTransferNotification(
            Transaction transaction,
            Wallet senderWallet,
            Wallet receiverWallet
    );
}
