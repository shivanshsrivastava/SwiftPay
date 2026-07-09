package com.walletsystem.swiftpay.common.exception;

public class TransactionNotFoundException  extends RuntimeException{
    public TransactionNotFoundException(String message){
        super(message);
    }
}
