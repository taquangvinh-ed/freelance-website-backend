package com.freelancemarketplace.backend.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseApplicationException {
    public ResourceNotFoundException(String message) {
        super(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND, message);
    }
}
