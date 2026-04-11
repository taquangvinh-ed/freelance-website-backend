package com.freelancemarketplace.backend.admin.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;

public class Q_AException extends BaseApplicationException {
    public Q_AException(String message) {
        super(ErrorCode.QA_ERROR, message);
    }

    public Q_AException(String message, Throwable cause){
        super(ErrorCode.QA_ERROR, org.springframework.http.HttpStatus.BAD_REQUEST, message, cause);
    }
}
