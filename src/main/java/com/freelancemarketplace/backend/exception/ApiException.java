package com.freelancemarketplace.backend.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends BaseApplicationException {

    public ApiException(HttpStatus httpStatus, ErrorCode errorCode, String message) {
        super(errorCode, httpStatus, message);
    }
}

