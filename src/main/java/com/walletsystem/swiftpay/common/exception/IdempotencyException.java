package com.walletsystem.swiftpay.common.exception;

public class IdempotencyException extends RuntimeException {

    public IdempotencyException(String message) {
        super(message);
    }
}