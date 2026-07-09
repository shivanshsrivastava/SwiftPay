package com.walletsystem.swiftpay.common.exception;

public class ConcurrentWalletModificationException
        extends RuntimeException {

    public ConcurrentWalletModificationException(
            String message
    ) {
        super(message);
    }
}