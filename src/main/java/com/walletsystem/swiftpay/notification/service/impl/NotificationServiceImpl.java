package com.walletsystem.swiftpay.notification.service.impl;

import com.walletsystem.swiftpay.notification.service.NotificationService;
import com.walletsystem.swiftpay.transaction.entity.Transaction;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    @Async("notificationExecutor")
    public void sendTransferNotification(
            Transaction transaction,
            Wallet senderWallet,
            Wallet receiverWallet
    ) {
        try {

            System.out.println("--------------------------------------");
            System.out.println("Preparing Notification...");
            System.out.println("Thread : " + Thread.currentThread().getName());

            Thread.sleep(3000);

            System.out.println("Transfer Notification Sent");
            System.out.println("Transaction : "
                    + transaction.getTransactionReference());

            System.out.println("Sender : "
                    + senderWallet.getWalletNumber());

            System.out.println("Receiver : "
                    + receiverWallet.getWalletNumber());

            System.out.println("--------------------------------------");

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(ex);
        }
    }
}
