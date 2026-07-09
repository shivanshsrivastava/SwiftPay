package com.walletsystem.swiftpay.wallet.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddMoneyRequest {

    @Positive
    private BigDecimal amount;
}