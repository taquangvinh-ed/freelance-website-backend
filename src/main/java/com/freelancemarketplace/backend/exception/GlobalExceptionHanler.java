package com.freelancemarketplace.backend.exception;

import com.freelancemarketplace.backend.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHanler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponseDTO>handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                    WebRequest webRequest){
    ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
            webRequest.getDescription(false),
            HttpStatus.NOT_FOUND,
            resourceNotFoundException.getMessage(),
            LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }
}
