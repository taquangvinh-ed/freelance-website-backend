package com.freelancemarketplace.backend.handler;

import com.freelancemarketplace.backend.model.UserModel;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message;
        int status = HttpStatus.UNAUTHORIZED.value();

        if (authException instanceof DisabledException) {
            message = "Tài khoản của bạn chưa được kích hoạt. Vui lòng kiểm tra email để xác thực.";
        }
        else if (authException instanceof LockedException) {
            // Lấy lý do từ UserDetails nếu có
            String reason = extractDisableReason(request);
            message = reason != null
                    ? "Tài khoản của bạn đã bị khóa. Lý do: " + reason
                    : "Tài khoản của bạn đã bị khóa hoặc bị cấm vĩnh viễn. Vui lòng liên hệ hỗ trợ.";
        }
        else if (authException instanceof CredentialsExpiredException) {
            message = "Mật khẩu đã hết hạn. Vui lòng đổi mật khẩu.";
        }
        else if (authException instanceof AccountExpiredException) {
            message = "Tài khoản đã hết hạn.";
        }
        else {
            message = "Tên đăng nhập hoặc mật khẩu không đúng.";
        }

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = """
            {
                "success": false,
                "message": "%s",
                "timestamp": "%s"
            }
            """.formatted(message, Instant.now());

        response.getWriter().write(json);
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
