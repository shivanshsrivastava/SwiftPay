package com.walletsystem.swiftpay.transaction.dto.response;

import com.walletsystem.swiftpay.transaction.entity.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferResponse {

    private String transactionReference;

    private BigDecimal amount;

    private TransactionStatus status;
}