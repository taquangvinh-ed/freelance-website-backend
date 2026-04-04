package com.freelancemarketplace.backend.domain.exception;

public class LanguageException extends BaseApplicationException {
    public LanguageException(String message) {
        super(ErrorCode.LANGUAGE_ERROR, message);
    }
}
