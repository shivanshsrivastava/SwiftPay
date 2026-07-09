package com.walletsystem.swiftpay.transaction.dto.response;

import com.walletsystem.swiftpay.transaction.entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsResponse {

    private String transactionReference;

    private String senderWalletNumber;

    private String receiverWalletNumber;

    private BigDecimal amount;

    private TransactionStatus status;

    private LocalDateTime createdAt;
}
