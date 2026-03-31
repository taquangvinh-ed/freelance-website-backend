package com.freelancemarketplace.backend.domain.exception;

public class AdminException extends BaseApplicationException {
    public AdminException(String message) {
        super(ErrorCode.ADMIN_ERROR, message);
    }
}
