package com.freelancemarketplace.backend.exception;

import org.springframework.http.HttpStatus;

public class SkillAlreadyExisted extends BaseApplicationException {
    public SkillAlreadyExisted(String message) {
        super(ErrorCode.SKILL_ALREADY_EXISTS, HttpStatus.CONFLICT, message);
    }
}
