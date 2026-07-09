package com.walletsystem.swiftpay.transaction.service.impl;

import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditEntityType;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import com.walletsystem.swiftpay.audit.service.AuditService;
import com.walletsystem.swiftpay.auth.repository.UserRepository;
import com.walletsystem.swiftpay.common.exception.InsufficientBalanceException;
import com.walletsystem.swiftpay.common.exception.InvalidWalletException;
import com.walletsystem.swiftpay.common.exception.TransactionNotFoundException;
import com.walletsystem.swiftpay.common.exception.WalletNotFoundException;
import com.walletsystem.swiftpay.common.service.CurrentUserService;
import com.walletsystem.swiftpay.common.util.TransactionReferenceGenerator;
import com.walletsystem.swiftpay.ledger.model.BalanceUpdateResult;
import com.walletsystem.swiftpay.ledger.service.LedgerService;
import com.walletsystem.swiftpay.security.util.SecurityUtils;
import com.walletsystem.swiftpay.transaction.dto.request.TransferRequest;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionDetailsResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionHistoryResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransferResponse;
import com.walletsystem.swiftpay.transaction.entity.Transaction;
import com.walletsystem.swiftpay.transaction.entity.TransactionStatus;
import com.walletsystem.swiftpay.transaction.mapper.TransactionMapper;
import com.walletsystem.swiftpay.transaction.repository.TransactionRepository;
import com.walletsystem.swiftpay.transaction.service.TransactionService;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import com.walletsystem.swiftpay.wallet.entity.enums.WalletStatus;
import com.walletsystem.swiftpay.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TransactionServiceImpl implements TransactionService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final TransactionReferenceGenerator referenceGenerator;
    private final TransactionMapper transactionMapper;
    private final LedgerService ledgerService;
    private final CurrentUserService currentUserService;
    private final AuditService auditService;

    // The below code is correct just commenting it out for cleaner structure and separation of concern

//    @Override
//    @Transactional
//    public TransferResponse transfer(
//            TransferRequest request
//    ) {
//
//        Wallet senderWallet = getCurrentUserWallet();
//
//        Wallet receiverWallet =
//                walletRepository.findByWalletNumber(
//                                request.getReceiverWalletNumber()
//                        )
//                        .orElseThrow(() ->
//                                new WalletNotFoundException(
//                                        "Receiver wallet not found"
//                                ));
//
//        validateTransfer(
//                senderWallet,
//                receiverWallet,
//                request.getAmount()
//        );
//
//        Transaction transaction =
//                Transaction.builder()
//                        .transactionReference(
//                                referenceGenerator.generateReference()
//                        )
//                        .senderWallet(senderWallet)
//                        .receiverWallet(receiverWallet)
//                        .amount(request.getAmount())
//                        .status(TransactionStatus.PENDING)
//                        .createdAt(LocalDateTime.now())
//                        .build();
//
//        transactionRepository.save(transaction);
//
//        senderWallet.setBalance(
//                senderWallet.getBalance()
//                        .subtract(request.getAmount())
//        );
//
//        receiverWallet.setBalance(
//                receiverWallet.getBalance()
//                        .add(request.getAmount())
//        );
//
//        walletRepository.save(senderWallet);
//        walletRepository.save(receiverWallet);
//
//        transaction.setStatus(
//                TransactionStatus.SUCCESS
//        );
//
//        transactionRepository.save(transaction);
//
//        return transactionMapper.toTransferResponse(transaction);
//

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        System.out.println("========== INSIDE transfer service() ==========");

        Wallet senderWallet = currentUserService.getCurrentUserWallet();

        return transfer(senderWallet, request);
    }

    @Transactional
    public TransferResponse transfer(
            Wallet senderWallet,
            TransferRequest request
    ) {

        String senderWalletNumber = senderWallet.getWalletNumber();
        String receiverWalletNumber = request.getReceiverWalletNumber();

        Wallet receiverWallet = null;


        try{

        receiverWallet = walletRepository.findByWalletNumber(
                        request.getReceiverWalletNumber()
                )
                .orElseThrow(() ->
                        new WalletNotFoundException(
                                "Receiver wallet not found."
                        ));

        validateTransfer(
                senderWallet,
                receiverWallet,
                request.getAmount()
        );

        Transaction transaction =
                createPendingTransaction(
                        senderWallet,
                        receiverWallet,
                        request.getAmount()
                );

        BalanceUpdateResult balanceUpdateResult = updateBalances(
                senderWallet,
                receiverWallet,
                request.getAmount()
        );

        ledgerService.recordTransferEntries(
                transaction,
                senderWallet,
                receiverWallet,
                balanceUpdateResult
        );

        transaction.setStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(transaction);

        auditService.recordAudit(

                AuditAction.TRANSFER,

                AuditEntityType.TRANSACTION,

                transaction.getTransactionReference(),

                String.format(
                        "Transferred %s from %s to %s",
                        transaction.getAmount(),
                        senderWalletNumber,
                        receiverWalletNumber
                ),

                AuditStatus.SUCCESS

        );


        return transactionMapper.toTransferResponse(transaction);
    } catch (RuntimeException ex){
            auditService.recordAudit(
                    AuditAction.TRANSFER,
                    AuditEntityType.TRANSACTION,
                    null,
                    String.format(
                            "Failed transfer of %s from %s to %s. Reason: %s",
                            request.getAmount(),
                            senderWallet.getWalletNumber(),
                            receiverWalletNumber,
                            ex.getMessage()
                    ),
                    AuditStatus.FAILED
            );

            throw ex;
        }
    }

    private Transaction createPendingTransaction(
            Wallet sender,
            Wallet receiver,
            BigDecimal amount
    ) {

        Transaction transaction = Transaction.builder()
                .transactionReference(
                        referenceGenerator.generateReference()
                )
                .senderWallet(sender)
                .receiverWallet(receiver)
                .amount(amount)
                .status(TransactionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    private BalanceUpdateResult updateBalances(
            Wallet sender,
            Wallet receiver,
            BigDecimal amount
    ) {

        BigDecimal senderBefore = sender.getBalance();
        BigDecimal receiverBefore = receiver.getBalance();

        sender.setBalance(
                senderBefore.subtract(amount)
        );

        receiver.setBalance(
                receiverBefore.add(amount)
        );

        walletRepository.save(sender);
        walletRepository.save(receiver);

        return BalanceUpdateResult.builder()
                .senderBalanceBefore(senderBefore)
                .senderBalanceAfter(sender.getBalance())
                .receiverBalanceBefore(receiverBefore)
                .receiverBalanceAfter(receiver.getBalance())
                .build();
    }



    ////        return TransferResponse.builder()
////                .transactionReference(
////                        transaction.getTransactionReference()
////                )
////                .amount(transaction.getAmount())
////                .status(transaction.getStatus())
////                .build();
//    }

    private void validateTransfer(
            Wallet senderWallet,
            Wallet receiverWallet,
            BigDecimal amount
    ) {

        if (senderWallet.getId().equals(receiverWallet.getId())) {
            throw new InvalidWalletException(
                    "Cannot transfer money to the same wallet."
            );
        }

        if (senderWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new InvalidWalletException(
                    "Sender wallet is not active."
            );
        }

        if (receiverWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new InvalidWalletException(
                    "Receiver wallet is not active."
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWalletException(
                    "Transfer amount must be greater than zero."
            );
        }

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient wallet balance."
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponse> getMyTransactions() {

        Wallet wallet = currentUserService.getCurrentUserWallet();

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderWalletIdOrReceiverWalletId(
                                wallet.getId(),
                                wallet.getId()
                        );

        return transactions.stream()
                .map(transaction ->
                        transactionMapper.toHistoryResponse(
                                transaction,
                                wallet
                        ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDetailsResponse
    getTransactionDetails(String reference) {

        Wallet currentWallet =
                currentUserService.getCurrentUserWallet();

        Transaction transaction =
                transactionRepository
                        .findByTransactionReference(reference)
                        .orElseThrow(() ->
                                new TransactionNotFoundException(
                                        "Transaction not found."
                                ));

        validateTransactionOwnership(
                currentWallet,
                transaction
        );

        return transactionMapper
                .toDetailsResponse(transaction);
    }

    private void validateTransactionOwnership(
            Wallet currentWallet,
            Transaction transaction
    ) {

        boolean isSender =
                transaction.getSenderWallet()
                        .getId()
                        .equals(currentWallet.getId());

        boolean isReceiver =
                transaction.getReceiverWallet()
                        .getId()
                        .equals(currentWallet.getId());

        if (!isSender && !isReceiver) {

            throw new AccessDeniedException(
                    "You are not authorized to view this transaction."
            );
        }
    }
}
//
//    private TransactionHistoryResponse mapToHistory(
//            Transaction transaction,
//            Wallet currentWallet
//    ) {
//
//        String type = transaction.getSenderWallet()
//                .getId()
//                .equals(currentWallet.getId())
//                ? "DEBIT"
//                : "CREDIT";
//
//        return TransactionHistoryResponse.builder()
//                .transactionReference(
//                        transaction.getTransactionReference()
//                )
//                .senderWalletNumber(
//                        transaction.getSenderWallet()
//                                .getWalletNumber()
//                )
//                .receiverWalletNumber(
//                        transaction.getReceiverWallet()
//                                .getWalletNumber()
//                )
//                .amount(transaction.getAmount())
//                .status(transaction.getStatus())
//                .createdAt(transaction.getCreatedAt())
//                .type(type)
//                .build();
//    }
//}
