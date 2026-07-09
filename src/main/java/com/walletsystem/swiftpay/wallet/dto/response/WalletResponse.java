package com.walletsystem.swiftpay.wallet.dto.response;

import com.walletsystem.swiftpay.wallet.entity.enums.WalletStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {

    private String walletNumber;

    private BigDecimal balance;

    private WalletStatus status;
}