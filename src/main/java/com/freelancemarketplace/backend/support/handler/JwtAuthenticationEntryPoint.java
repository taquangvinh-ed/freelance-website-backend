package com.freelancemarketplace.backend.support.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message;
        ErrorCode errorCode;

        if (authException instanceof DisabledException) {
            message = "Tai khoan cua ban chua duoc kich hoat. Vui long kiem tra email de xac thuc.";
            errorCode = ErrorCode.UNAUTHORIZED;
        }
        else if (authException instanceof LockedException) {
            // Lấy lý do từ UserDetails nếu có
            String reason = extractDisableReason(request);
            message = reason != null
                    ? "Tai khoan cua ban da bi khoa. Ly do: " + reason
                    : "Tai khoan cua ban da bi khoa hoac bi cam vinh vien. Vui long lien he ho tro.";
            errorCode = ErrorCode.UNAUTHORIZED;
        }
        else if (authException instanceof CredentialsExpiredException) {
            message = "Mat khau da het han. Vui long doi mat khau.";
            errorCode = ErrorCode.UNAUTHORIZED;
        }
        else if (authException instanceof AccountExpiredException) {
            message = "Tai khoan da het han.";
            errorCode = ErrorCode.UNAUTHORIZED;
        }
        else {
            message = ErrorCode.INVALID_CREDENTIALS.getMessage();
            errorCode = ErrorCode.INVALID_CREDENTIALS;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<?> apiResponse = ApiResponse.error(errorCode.getCode(), message, null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private String extractDisableReason(HttpServletRequest request) {
        Authentication authentication = (Authentication) request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION_AUTHENTICATION");
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            // Nếu bạn lưu disableReason trong UserModel, có thể cast hoặc dùng attribute
            if (userDetails instanceof UserModel user) {
                return user.getDisableReason();
            }
        }
        return null;
    }
}
