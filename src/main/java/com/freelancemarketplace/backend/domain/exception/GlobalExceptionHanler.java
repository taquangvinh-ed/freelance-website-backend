package com.freelancemarketplace.backend.domain.exception;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHanler {

//    @ExceptionHandler(ResourceNotFoundException.class)
//    ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
//                                                                      WebRequest webRequest){
//    ApiResponse<Void> response = ApiResponse.error(
//            HttpStatus.NOT_FOUND.value(),
//            resourceNotFoundException.getMessage(),
//            null
//    );
//    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(SkillAlreadyExisted.class)
//    ResponseEntity<ApiResponse<Void>> handleCustomerAlreadyExistException(SkillAlreadyExisted skillAlreadyExisted,
//                                                                          WebRequest webRequest){
//        ApiResponse<Void> response = ApiResponse.error(
//                HttpStatus.BAD_REQUEST.value(),
//                skillAlreadyExisted.getMessage(),
//                null
//        );
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(ProposalException.class)
//    ResponseEntity<ApiResponse<Void>> handleProposalException(ProposalException proposalException,
//                                                              WebRequest request){
//        ApiResponse<Void> response = ApiResponse.error(
//                HttpStatus.BAD_REQUEST.value(),
//                proposalException.getMessage(),
//                null
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(response);
//    }
//
//    @ExceptionHandler(UserException.class)
//    ResponseEntity<ApiResponse<Void>> handleUserException(WebRequest request, UserException exception){
//        ApiResponse<Void> response = ApiResponse.error(
//                HttpStatus.BAD_REQUEST.value(),
//                exception.getMessage(),
//                null
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(response);
//    }

}
