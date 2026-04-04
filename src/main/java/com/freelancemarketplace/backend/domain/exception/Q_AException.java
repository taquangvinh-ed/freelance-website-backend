package com.freelancemarketplace.backend.domain.exception;

public class Q_AException extends BaseApplicationException {
    public Q_AException(String message) {
        super(ErrorCode.QA_ERROR, message);
    }

    public Q_AException(String message, Throwable cause){
        super(ErrorCode.QA_ERROR, org.springframework.http.HttpStatus.BAD_REQUEST, message, cause);
    }
}
