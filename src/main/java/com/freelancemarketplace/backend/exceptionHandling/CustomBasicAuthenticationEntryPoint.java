package com.freelancemarketplace.backend.exceptionHandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {


        Timestamp currentTime = Timestamp.from(Instant.now());
        String message = (authException != null && !authException.getMessage().isEmpty()) ? authException.getMessage() : "UNTHORIZED";
        String path = request.getRequestURI();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("freelancer-error-reason: ", "Authentication failed");
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = String.format("{\"timestampe\":\"%s\",\"status\":\"%d\",\"error\":\"%s\",\"message\":\"%s\",\"paht\":\"%s}",
                currentTime, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message, path);

        response.getWriter().write(jsonResponse);
    }
}
