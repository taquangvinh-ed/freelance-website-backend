package com.freelancemarketplace.backend.email.application.service;

import org.springframework.messaging.MessagingException;

public interface EmailService {
    public void sendSimpleEmail(Long clientId,  Long freelancerId, Long projectId,  String to, String projectTitle, String text);
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;
}
