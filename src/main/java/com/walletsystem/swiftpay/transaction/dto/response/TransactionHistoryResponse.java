package com.walletsystem.swiftpay.transaction.dto.response;

import com.walletsystem.swiftpay.transaction.entity.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionHistoryResponse {

    private String transactionReference;

    private String senderWalletNumber;

    private String receiverWalletNumber;

    private BigDecimal amount;

    private TransactionStatus status;

    private LocalDateTime createdAt;

    private String type;
}