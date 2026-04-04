package com.freelancemarketplace.backend.contract.exception;

public class MilestoneNotFoundException extends RuntimeException {
    public MilestoneNotFoundException(String message) {
        super(message);
    }
}

