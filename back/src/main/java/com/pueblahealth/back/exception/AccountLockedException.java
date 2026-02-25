package com.pueblahealth.back.exception;

public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message) {
        super(message);
    }
}