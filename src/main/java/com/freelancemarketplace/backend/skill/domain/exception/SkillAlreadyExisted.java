package com.freelancemarketplace.backend.skill.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import org.springframework.http.HttpStatus;

public class SkillAlreadyExisted extends BaseApplicationException {
    public SkillAlreadyExisted(String message) {
        super(ErrorCode.SKILL_ALREADY_EXISTS, HttpStatus.CONFLICT, message);
    }
}
