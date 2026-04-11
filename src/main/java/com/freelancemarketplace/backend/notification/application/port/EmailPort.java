package com.freelancemarketplace.backend.notification.application.port;

/**
 * Port: Email Service Interface
 */
public interface EmailPort {
    
    /**
     * Send email to recipient
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body (HTML or plain text)
     */
    void sendEmail(String to, String subject, String body);
    
    /**
     * Send email with template
     * @param to Recipient email address
     * @param subject Email subject
     * @param template Template name
     * @param variables Template variables
     */
    void sendEmailFromTemplate(String to, String subject, String template, java.util.Map<String, Object> variables);
    
    /**
     * Send OTP email
     * @param to Recipient email address
     * @param otp OTP code
     */
    void sendOTPEmail(String to, String otp);
}

