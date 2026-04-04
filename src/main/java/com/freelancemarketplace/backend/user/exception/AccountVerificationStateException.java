package com.freelancemarketplace.backend.user.exception;

public class AccountVerificationStateException extends RuntimeException {
    public AccountVerificationStateException(String message) {
        super(message);
    }
}

