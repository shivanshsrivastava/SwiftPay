package com.walletsystem.swiftpay.common.exception;

public class InsufficientBalanceException
        extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}