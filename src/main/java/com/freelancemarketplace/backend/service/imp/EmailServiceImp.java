package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String projectTitle, String text) {
        String subject = "Mời tham gia đấu thầu dự án: " + (projectTitle != null ? projectTitle : "");
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send simple email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            // Tham số thứ hai (true) chỉ ra rằng nội dung là HTML
//            helper.setText(htmlContent, true);
//
//            mailSender.send(message);
//            log.info("HTML Email sent successfully to: {}", to);
//        } catch (MessagingException e) {
//            log.error("Failed to send HTML email to {}: {}", to, e.getMessage());
//            throw e;
//        }
    }
}
