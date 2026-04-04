package com.freelancemarketplace.backend.skill.exception;

public class SkillNotFoundException extends RuntimeException {
    public SkillNotFoundException(String message) {
        super(message);
    }
}

