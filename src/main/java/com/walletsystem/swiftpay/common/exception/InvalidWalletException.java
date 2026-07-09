package com.walletsystem.swiftpay.common.exception;

public class InvalidWalletException
        extends RuntimeException {

    public InvalidWalletException(String message) {
        super(message);
    }
}