package com.freelancemarketplace.backend.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    SUCCESS(2000, "Request successful"),
    CREATE_SUCCESS(2001, "Resource created successfully"),
    NO_CONTENT(2002, "No content");

    private final int code;
    private final String message;
}

