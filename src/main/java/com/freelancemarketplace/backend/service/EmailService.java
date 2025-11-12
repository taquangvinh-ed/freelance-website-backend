package com.freelancemarketplace.backend.service;

import org.springframework.messaging.MessagingException;

public interface EmailService {
    public void sendSimpleEmail(String to, String projectTitle, String text);
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;
}
