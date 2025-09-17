package com.freelancemarketplace.backend.exception;

public class SkillAlreadyExisted extends RuntimeException {
    public SkillAlreadyExisted(String message) {
        super(message);
    }
}
