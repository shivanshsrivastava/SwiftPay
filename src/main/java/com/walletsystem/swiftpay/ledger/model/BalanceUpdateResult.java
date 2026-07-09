package com.walletsystem.swiftpay.ledger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class BalanceUpdateResult {

    private final BigDecimal senderBalanceBefore;

    private final BigDecimal senderBalanceAfter;

    private final BigDecimal receiverBalanceBefore;

    private final BigDecimal receiverBalanceAfter;

}