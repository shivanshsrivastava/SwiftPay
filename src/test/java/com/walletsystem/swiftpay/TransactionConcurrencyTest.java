package com.walletsystem.swiftpay;

import com.walletsystem.swiftpay.auth.entity.Role;
import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.auth.repository.UserRepository;
import com.walletsystem.swiftpay.transaction.dto.request.TransferRequest;
import com.walletsystem.swiftpay.transaction.repository.TransactionRepository;
import com.walletsystem.swiftpay.transaction.service.impl.TransactionServiceImpl;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import com.walletsystem.swiftpay.wallet.entity.enums.WalletStatus;
import com.walletsystem.swiftpay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionConcurrencyTest {

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private User senderUser;
    private User receiverUser;

    private Wallet senderWallet;
    private Wallet receiverWallet;

    @BeforeEach
    void setUp() {

        // Clean previous test data
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();

        //------------------------------------------
        // Create Sender User
        //------------------------------------------

        senderUser = User.builder()
                .fullName("Sender User")
                .email("sender@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        //------------------------------------------
        // Create Receiver User
        //------------------------------------------

        receiverUser = User.builder()
                .fullName("Receiver User")
                .email("receiver@test.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        senderUser = userRepository.save(senderUser);
        receiverUser = userRepository.save(receiverUser);

        //------------------------------------------
        // Create Sender Wallet
        //------------------------------------------

        senderWallet = Wallet.builder()
                .walletNumber("SPW-SENDER")
                .balance(new BigDecimal("1000.00"))
                .status(WalletStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .user(senderUser)
                .build();

        //------------------------------------------
        // Create Receiver Wallet
        //------------------------------------------

        receiverWallet = Wallet.builder()
                .walletNumber("SPW-RECEIVER")
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .user(receiverUser)
                .build();

        senderWallet = walletRepository.save(senderWallet);
        receiverWallet = walletRepository.save(receiverWallet);

        //------------------------------------------
        // Verify Initial State
        //------------------------------------------

        assertEquals(
                new BigDecimal("1000.00"),
                senderWallet.getBalance()
        );

        assertEquals(
                BigDecimal.ZERO,
                receiverWallet.getBalance()
        );
    }

    @Test
    void shouldHandleConcurrentTransfersCorrectly() throws Exception {

        TransferRequest request = new TransferRequest();

        request.setReceiverWalletNumber(
                receiverWallet.getWalletNumber()
        );

        request.setAmount(new BigDecimal("200"));

        int numberOfThreads = 10;

        ExecutorService executorService =
                Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch =
                new CountDownLatch(1);

        CountDownLatch finishLatch =
                new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger();

        AtomicInteger failureCount = new AtomicInteger();

        for (int i = 0; i < numberOfThreads; i++) {

            executorService.submit(() -> {

                try {

                    startLatch.await();

                    Wallet currentSender =
                            walletRepository.findById(senderWallet.getId())
                                    .orElseThrow();

                    transactionService.transfer(
                            currentSender,
                            request
                    );

                    successCount.incrementAndGet();

                } catch (Exception ex) {

                    failureCount.incrementAndGet();

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " -> "
                                    + ex.getClass().getSimpleName()
                                    + " : "
                                    + ex.getMessage()
                    );
                } finally {

                    finishLatch.countDown();

                }

            });

        }

        startLatch.countDown();

        finishLatch.await();

        executorService.shutdown();

    }


}