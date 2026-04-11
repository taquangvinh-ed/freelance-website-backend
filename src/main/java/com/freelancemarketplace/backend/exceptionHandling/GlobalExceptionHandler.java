package com.freelancemarketplace.backend.exceptionHandling;

import com.freelancemarketplace.backend.admin.exception.AdminException;
import com.freelancemarketplace.backend.admin.exception.AdminNotFoundException;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.client.exception.ClientNotFoundException;
import com.freelancemarketplace.backend.contract.exception.ContractNotFoundException;
import com.freelancemarketplace.backend.contract.exception.MilestoneNotFoundException;
import com.freelancemarketplace.backend.email.exception.EmailSendException;
import com.freelancemarketplace.backend.freelancer.exception.FreelancerNotFoundException;
import com.freelancemarketplace.backend.language.exception.LanguageException;
import com.freelancemarketplace.backend.location.exception.LocationNotFoundException;
import com.freelancemarketplace.backend.notification.exception.NotificationException;
import com.freelancemarketplace.backend.notification.exception.NotificationNotFoundException;
import com.freelancemarketplace.backend.payment.exception.PaymentNotFoundException;
import com.freelancemarketplace.backend.product.exception.ProductNotFoundException;
import com.freelancemarketplace.backend.project.exception.BudgetNotFoundException;
import com.freelancemarketplace.backend.project.exception.ProjectNotFoundException;
import com.freelancemarketplace.backend.project.exception.ProjectQuestionNotFoundException;
import com.freelancemarketplace.backend.proposal.exception.ProposalException;
import com.freelancemarketplace.backend.review.exception.ReviewOperationNotAllowedException;
import com.freelancemarketplace.backend.skill.exception.SkillAlreadyExisted;
import com.freelancemarketplace.backend.skill.exception.SkillNotFoundException;
import com.freelancemarketplace.backend.user.exception.AccountVerificationStateException;
import com.freelancemarketplace.backend.user.exception.InvalidOtpException;
import com.freelancemarketplace.backend.user.exception.OtpEmailSendException;
import com.freelancemarketplace.backend.user.exception.UserException;
import com.freelancemarketplace.backend.user.exception.UserNotFoundException;
import com.freelancemarketplace.backend.user.exception.VerificationNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex) {
        ErrorCode errorCode = ex.getErrorCode() != null ? ex.getErrorCode() : ErrorCode.INVALID_REQUEST;
        HttpStatus httpStatus = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.BAD_REQUEST;

        ApiResponse<?> response = ApiResponse.error(
                errorCode.getCode(),
                ex.getMessage() != null ? ex.getMessage() : errorCode.getMessage(),
                null
        );
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAdminNotFoundException(AdminNotFoundException ex) {
        log.error("Admin not found: {}", ex.getMessage());
        ErrorCode errorCode = ErrorCode.ADMIN_NOT_FOUND;

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(
                errorCode.getCode(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        log.error("Notification not found: {}", ex.getMessage());
        ErrorCode errorCode = ErrorCode.NOTIFICATION_NOT_FOUND;

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(
                errorCode.getCode(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ApiResponse<?>> handleNotificationException(NotificationException ex) {
        log.error("Notification error: {}", ex.getMessage());
        ErrorCode errorCode = ErrorCode.NOTIFICATION_ERROR;

        return ResponseEntity.badRequest().body(ApiResponse.error(
                errorCode.getCode(),
                ex.getMessage(),
                null
        ));
    }

    @ExceptionHandler({
            ClientNotFoundException.class,
            ContractNotFoundException.class,
            MilestoneNotFoundException.class,
            PaymentNotFoundException.class,
            FreelancerNotFoundException.class,
            ProductNotFoundException.class,
            LocationNotFoundException.class,
            SkillNotFoundException.class,
            ProjectNotFoundException.class,
            ProjectQuestionNotFoundException.class,
            BudgetNotFoundException.class,
            VerificationNotFoundException.class,
            UserNotFoundException.class,
            ResourceNotFoundException.class
    })
    public ResponseEntity<ApiResponse<?>> handleNotFoundExceptions(RuntimeException ex) {
        ErrorCode errorCode = resolveNotFoundErrorCode(ex);

        ApiResponse<?> response = ApiResponse.error(
                errorCode.getCode(),
                ex.getMessage() != null ? ex.getMessage() : errorCode.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({
            AdminException.class,
            ProposalException.class,
            LanguageException.class,
            EmailSendException.class,
            SkillAlreadyExisted.class,
            UserException.class,
            AccountVerificationStateException.class,
            InvalidOtpException.class,
            OtpEmailSendException.class,
            ReviewOperationNotAllowedException.class
    })
    public ResponseEntity<ApiResponse<?>> handleBusinessExceptions(RuntimeException ex) {
        ErrorCode errorCode = resolveBusinessErrorCode(ex);
        HttpStatus status = ex instanceof SkillAlreadyExisted ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;

        ApiResponse<?> response = ApiResponse.error(
                errorCode.getCode(),
                ex.getMessage() != null ? ex.getMessage() : errorCode.getMessage(),
                null
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(m -> m != null && !m.isBlank())
                .collect(Collectors.joining(", "));

        if (message.isBlank()) {
            message = ErrorCode.VALIDATION_ERROR.getMessage();
        }

        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.VALIDATION_ERROR.getCode(),
                message,
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.VALIDATION_ERROR.getCode(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.INVALID_REQUEST.getCode(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalStateException(IllegalStateException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.INVALID_OPERATION.getCode(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<?>> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.EXPIRED_JWT.getCode(),
                ErrorCode.EXPIRED_JWT.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.FORBIDDEN.getCode(),
                ErrorCode.FORBIDDEN.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        log.error("Unhandled runtime exception", ex);
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.INVALID_OPERATION.getCode(),
                ex.getMessage() != null ? ex.getMessage() : ErrorCode.INVALID_OPERATION.getMessage(),
                null
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        ApiResponse<?> response = ApiResponse.error(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ErrorCode resolveNotFoundErrorCode(RuntimeException ex) {
        if (ex instanceof UserNotFoundException) return ErrorCode.USER_NOT_FOUND;
        if (ex instanceof ClientNotFoundException) return ErrorCode.CLIENT_NOT_FOUND;
        if (ex instanceof FreelancerNotFoundException) return ErrorCode.FREELANCER_NOT_FOUND;
        if (ex instanceof ContractNotFoundException) return ErrorCode.CONTRACT_NOT_FOUND;
        if (ex instanceof MilestoneNotFoundException) return ErrorCode.MILESTONE_NOT_FOUND;
        if (ex instanceof PaymentNotFoundException) return ErrorCode.PAYMENT_NOT_FOUND;
        if (ex instanceof ProductNotFoundException) return ErrorCode.PRODUCT_NOT_FOUND;
        if (ex instanceof LocationNotFoundException) return ErrorCode.LOCATION_NOT_FOUND;
        if (ex instanceof SkillNotFoundException) return ErrorCode.SKILL_NOT_FOUND;
        if (ex instanceof ProjectQuestionNotFoundException) return ErrorCode.PROJECT_QUESTION_NOT_FOUND;
        if (ex instanceof BudgetNotFoundException) return ErrorCode.BUDGET_NOT_FOUND;
        if (ex instanceof VerificationNotFoundException) return ErrorCode.VERIFICATION_NOT_FOUND;
        return ErrorCode.PROJECT_NOT_FOUND;
    }

    private ErrorCode resolveBusinessErrorCode(RuntimeException ex) {
        if (ex instanceof AdminException) return ErrorCode.ADMIN_ERROR;
        if (ex instanceof ProposalException) return ErrorCode.PROPOSAL_ERROR;
        if (ex instanceof LanguageException) return ErrorCode.LANGUAGE_ERROR;
        if (ex instanceof EmailSendException || ex instanceof OtpEmailSendException) return ErrorCode.EMAIL_SEND_FAILED;
        if (ex instanceof SkillAlreadyExisted) return ErrorCode.SKILL_ALREADY_EXISTS;
        if (ex instanceof InvalidOtpException) return ErrorCode.INVALID_OTP;
        if (ex instanceof ReviewOperationNotAllowedException) return ErrorCode.REVIEW_OPERATION_NOT_ALLOWED;
        return ErrorCode.INVALID_OPERATION;
    }

}

