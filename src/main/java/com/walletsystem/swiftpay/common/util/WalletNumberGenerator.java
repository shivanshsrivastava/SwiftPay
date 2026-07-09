package com.walletsystem.swiftpay.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WalletNumberGenerator {

    public String generateWalletNumber() {

        return "SPW-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}