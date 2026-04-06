package com.freelancemarketplace.backend.notification.application.service.imp;

import com.freelancemarketplace.backend.notification.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.notification.dto.NotificationDTO;

import com.freelancemarketplace.backend.admin.exception.AdminNotFoundException;
import com.freelancemarketplace.backend.admin.infrastructure.repository.AdminsRepository;
import com.freelancemarketplace.backend.notification.exception.NotificationException;
import com.freelancemarketplace.backend.notification.exception.NotificationNotFoundException;
import com.freelancemarketplace.backend.notification.infrastructure.mapper.ClarificationProjectNotificationMapper;
import com.freelancemarketplace.backend.notification.infrastructure.mapper.NotificationMapper;
import com.freelancemarketplace.backend.admin.domain.model.AdminModel;
import com.freelancemarketplace.backend.notification.domain.model.ClarificationProjectQANotificationModel;
import com.freelancemarketplace.backend.notification.domain.model.NotificationModel;
import com.freelancemarketplace.backend.notification.infrastructure.repository.NotificationsRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectClarificationNotificationRepository;
import com.freelancemarketplace.backend.notification.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService {
    private final AdminsRepository adminsRepository;
    private final ProjectClarificationNotificationRepository projectClarificationNotificationRepository;
    private final ClarificationProjectNotificationMapper clarificationProjectNotificationMapper;

    Logger logger = LoggerFactory.getLogger(NotificationServiceImp.class);

    private final NotificationsRepository notificationsRepository;
    private final NotificationMapper notificationMapper;



    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {

        NotificationModel notificationModel = notificationMapper.toEntity(notificationDTO);

        AdminModel adminModel = adminsRepository.findById(notificationDTO.getAdminId()).orElseThrow(
                () -> new AdminNotFoundException("Admin with id: " + notificationDTO.getAdminId() + " not found")
        );
        notificationModel.setAdmin(adminModel);
        NotificationModel savedNotification = notificationsRepository.save(notificationModel);
        logger.info("Notificacation created successfully with id: {}", savedNotification.getNotificationId());
        return notificationMapper.toDTO(savedNotification);

    }

    @Override
    public NotificationDTO updateNotification(Long notificationId, NotificationDTO notificationDTO) {
        NotificationModel notificationModel = notificationsRepository.findById(notificationId).orElseThrow(
                () -> new NotificationNotFoundException("Notification with id: " + notificationId + " not found")
        );


        if (notificationDTO.getTitle() != null)
            notificationModel.setTitle(notificationDTO.getTitle());

        if (notificationDTO.getDescription() != null)
            notificationModel.setDescription(notificationDTO.getDescription());

        if (notificationDTO.getImageUrl() != null)
            notificationModel.setImageUrl(notificationDTO.getImageUrl());

        NotificationModel savedNotification = notificationsRepository.save(notificationModel);
        logger.info("Notification with id: {} has already updated successfully", savedNotification.getNotificationId());
        return notificationMapper.toDTO(savedNotification);


    }

    @Override
    public Boolean deleteNotification(Long notificationId) {
        if (notificationsRepository.existsById(notificationId)) {
            notificationsRepository.deleteById(notificationId);
            logger.info("Notification with id: {} has already deleted", notificationId);
            return true;

        } else {
            logger.error("Notificaiton with id: {} not found", notificationId);
            throw new NotificationException("Message: notificaiton with id: " + notificationId + " not found");

        }
    }

    @Override
    public List<NotificationDTO> getAllNotification() {
        List<NotificationModel> notifications = notificationsRepository.findAll();
        List<NotificationDTO> notificationDTOs = notificationMapper.toDTOs(notifications);
        return notificationDTOs;


    }

    @Override
    public void markClarificationPrjectQANotificationAsRead(Long projectClarificationNotificationId) {
        ClarificationProjectQANotificationModel notification = projectClarificationNotificationRepository.findById(projectClarificationNotificationId).orElseThrow(()->
                new NotificationNotFoundException("Clarification project QA notification with id: " + projectClarificationNotificationId + " not found"));

        if(notification.isRead())
            logger.info("Clarification project QA notification with id: {} is already marked as read", projectClarificationNotificationId);
        else {
            notification.setRead(true);
            notification.setReadAt(Timestamp.from(Instant.now()));
            projectClarificationNotificationRepository.save(notification);
            logger.info("Clarification project QA notification with id: {} has already marked as read", projectClarificationNotificationId);
        }
    }

    @Override
    public List<ClarificationiProjectQANotificationDTO> getClarificationProjectQANotificationsByUserId(Long userId) {

        List<ClarificationProjectQANotificationModel> notifications = projectClarificationNotificationRepository.findTop10ByRecipientUserIdOrderByCreatedAtDesc(userId);

        return clarificationProjectNotificationMapper.toDTOs(notifications);
    }
}
