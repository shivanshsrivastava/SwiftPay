package com.walletsystem.swiftpay.common.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String message){
        super(message);
    }
}
