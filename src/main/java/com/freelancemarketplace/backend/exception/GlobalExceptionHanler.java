package com.freelancemarketplace.backend.exception;

import com.freelancemarketplace.backend.dto.ErrorResponseDTO;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import org.springframework.http.HttpRequest;
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

    @ExceptionHandler(SkillAlreadyExisted.class)
    ResponseEntity<ErrorResponseDTO>handleCustomerAlreadyExistException(SkillAlreadyExisted skillAlreadyExisted,
                                                                        WebRequest webRequest){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                skillAlreadyExisted.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProposalException.class)
    ResponseEntity<ErrorResponseDTO>handleProposalException(ProposalException proposalException,
                                                            WebRequest request){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                proposalException.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDTO);
    }

    @ExceptionHandler(UserException.class)
    ResponseEntity<ErrorResponseDTO>handleUserException(WebRequest request, UserException exception){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                request.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponseDTO);
    }

}
