package com.freelancemarketplace.backend.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private String apiPath;

    private HttpStatus errorCode;

    private String messageError;

    private LocalDateTime errorTime;

    public ErrorResponseDTO(String apiPath, HttpStatus errorCode, String messageError, LocalDateTime errorTime) {
        this.apiPath = apiPath;
        this.errorCode = errorCode;
        this.messageError = messageError;
        this.errorTime = errorTime;
    }
}
