package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.enums.InvitationStatus;
import com.freelancemarketplace.backend.exception.EmailSendException;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.InvitationModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.InvitationRepository;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import com.freelancemarketplace.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;
    private final ClientsRepository clientsRepository;
    private final FreelancersRepository freelancersRepository;
    private final ProjectsRepository projectsRepository;
    private final InvitationRepository invitationRepository;


    @Override
    public void sendSimpleEmail(Long clientId, Long freelancerId, Long projectId, String to, String projectTitle, String text) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                ()-> new ResourceNotFoundException("Client with id: " + clientId + " not found")
        );

        FreelancerModel freelancerModel = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                ()-> new ResourceNotFoundException("Project with id: " + projectId + " not found")
        );

        String clientEmail = null;

        if(client.getUser() != null)
            clientEmail = client.getUser().getEmail();

        if (!isValidEmail(to)) {
            log.warn("Invalid email address: {}", to);
            throw new IllegalArgumentException("Invalid email address: " + to);
        }

        String subject = "Mời tham gia đấu thầu dự án: " + (projectTitle != null ? projectTitle : "Không có tiêu đề");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("taquangvinh.study@gmail.com");
        message.setReplyTo(clientEmail != null ? clientEmail : "freelancerhub@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
            InvitationModel newInvitation = new InvitationModel();
            newInvitation.setFreelancer(freelancerModel);
            newInvitation.setProject(project);
            newInvitation.setInvitedAt(LocalDateTime.now());
            newInvitation.setStatus(InvitationStatus.SENT);
            invitationRepository.save(newInvitation);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new EmailSendException("Could not send email to " + e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
        try {
            log.debug("Starting to send HTML email to: {}", to);
            log.debug("Subject: {}", subject);

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("taquangvinh.study@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);

            // Tham số thứ hai (true) chỉ ra rằng nội dung là HTML
            helper.setText(htmlContent, true);

            log.debug("Message prepared, attempting to send...");
            mailSender.send(message);
            log.info("HTML Email sent successfully to: {} with subject: {}", to, subject);
        } catch (jakarta.mail.MessagingException e) {
            log.error("Failed to send HTML email to {}: {} - Cause: {}", to, e.getMessage(), e.getCause(), e);
            throw new MessagingException(e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while sending HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        boolean isValid = email != null && email.matches(emailRegex);
        if (!isValid) {
            log.warn("Email validation failed for: {}", email);
        }
        return isValid;
    }
}
