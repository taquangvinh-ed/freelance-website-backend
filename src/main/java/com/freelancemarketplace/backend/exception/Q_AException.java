package com.freelancemarketplace.backend.exception;

public class Q_AException extends RuntimeException {
    public Q_AException(String message) {
        super(message);
    }

    public Q_AException(String message, Throwable cause){
        super(message, cause);
    }
}
