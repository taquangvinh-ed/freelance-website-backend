package com.freelancemarketplace.backend.admin.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;

public class AdminException extends BaseApplicationException {
    public AdminException(String message) {
        super(ErrorCode.ADMIN_ERROR, message);
    }
}
