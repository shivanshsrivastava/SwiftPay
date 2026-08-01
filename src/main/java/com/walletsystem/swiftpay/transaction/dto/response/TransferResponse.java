package com.walletsystem.swiftpay.transaction.dto.response;

import com.walletsystem.swiftpay.transaction.entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponse {

    private String transactionReference;

    private BigDecimal amount;

    private TransactionStatus status;
}