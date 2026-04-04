package com.freelancemarketplace.backend.skill.exception;

public class SkillAlreadyExisted extends RuntimeException {
    public SkillAlreadyExisted(String message) {
        super(message);
    }
}
