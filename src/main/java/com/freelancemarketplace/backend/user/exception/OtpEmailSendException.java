package com.freelancemarketplace.backend.user.exception;

public class OtpEmailSendException extends RuntimeException {
    public OtpEmailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}

