package com.freelancemarketplace.backend.freelancer.exception;

public class FreelancerNotFoundException extends RuntimeException {
    public FreelancerNotFoundException(String message) {
        super(message);
    }
}

