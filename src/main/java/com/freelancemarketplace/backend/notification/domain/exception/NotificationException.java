package com.freelancemarketplace.backend.notification.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;

public class NotificationException extends BaseApplicationException {
    public NotificationException(String message) {
        super(ErrorCode.NOTIFICATION_ERROR, message);
    }
    public NotificationException(String message, Throwable cause){
        super(ErrorCode.NOTIFICATION_ERROR, org.springframework.http.HttpStatus.BAD_REQUEST, message, cause);
    }
}
