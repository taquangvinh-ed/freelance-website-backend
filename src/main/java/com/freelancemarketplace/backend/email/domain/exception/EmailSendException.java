package com.freelancemarketplace.backend.email.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;

public class EmailSendException extends BaseApplicationException {
    public EmailSendException(String message) {
        super(ErrorCode.EMAIL_SEND_FAILED, message);
    }
}
