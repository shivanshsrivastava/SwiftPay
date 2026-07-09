package com.walletsystem.swiftpay.ledger.dto;

import com.walletsystem.swiftpay.ledger.entity.LedgerEntryType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LedgerEntryResponse {

    private String walletNumber;

    private LedgerEntryType entryType;

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private LocalDateTime createdAt;

}