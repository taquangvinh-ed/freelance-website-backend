package com.freelancemarketplace.backend.exception;

public class NotificationException extends BaseApplicationException {
    public NotificationException(String message) {
        super(ErrorCode.NOTIFICATION_ERROR, message);
    }
    public NotificationException(String message, Throwable cause){
        super(ErrorCode.NOTIFICATION_ERROR, org.springframework.http.HttpStatus.BAD_REQUEST, message, cause);
    }
}
