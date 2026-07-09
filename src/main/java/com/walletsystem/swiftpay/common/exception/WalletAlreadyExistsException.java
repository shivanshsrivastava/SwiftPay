package com.walletsystem.swiftpay.common.exception;

public class WalletAlreadyExistsException
        extends RuntimeException {

    public WalletAlreadyExistsException(String message) {
        super(message);
    }
}