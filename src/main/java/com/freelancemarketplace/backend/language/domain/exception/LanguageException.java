package com.freelancemarketplace.backend.language.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;

public class LanguageException extends BaseApplicationException {
    public LanguageException(String message) {
        super(ErrorCode.LANGUAGE_ERROR, message);
    }
}
