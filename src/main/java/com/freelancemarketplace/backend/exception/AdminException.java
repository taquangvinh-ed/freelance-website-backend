package com.freelancemarketplace.backend.exception;

public class AdminException extends BaseApplicationException {
    public AdminException(String message) {
        super(ErrorCode.ADMIN_ERROR, message);
    }
}
