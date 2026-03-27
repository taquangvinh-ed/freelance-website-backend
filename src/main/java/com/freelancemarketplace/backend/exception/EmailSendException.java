package com.freelancemarketplace.backend.exception;

public class EmailSendException extends BaseApplicationException {
    public EmailSendException(String message) {
        super(ErrorCode.EMAIL_SEND_FAILED, message);
    }
}
