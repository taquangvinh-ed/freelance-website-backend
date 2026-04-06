package com.freelancemarketplace.backend.review.exception;

public class ReviewOperationNotAllowedException extends RuntimeException {
    public ReviewOperationNotAllowedException(String message) {
        super(message);
    }
}

