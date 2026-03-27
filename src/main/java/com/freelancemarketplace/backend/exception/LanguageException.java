package com.freelancemarketplace.backend.exception;

public class LanguageException extends BaseApplicationException {
    public LanguageException(String message) {
        super(ErrorCode.LANGUAGE_ERROR, message);
    }
}
