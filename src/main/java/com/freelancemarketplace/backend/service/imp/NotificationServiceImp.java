package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.NotificationDTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.NotificationException;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.NotificationMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.NotificationModel;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.repository.NotificationsRepository;
import com.freelancemarketplace.backend.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService {
    private final AdminsRepository adminsRepository;

    Logger logger = LoggerFactory.getLogger(NotificationServiceImp.class);

    private final NotificationsRepository notificationsRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImp(NotificationsRepository notificationsRepository, NotificationMapper notificationMapper,
                                  AdminsRepository adminsRepository) {
        this.notificationsRepository = notificationsRepository;
        this.notificationMapper = notificationMapper;
        this.adminsRepository = adminsRepository;
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {

        NotificationModel notificationModel = notificationMapper.toEntity(notificationDTO);

        AdminModel adminModel = adminsRepository.findById(notificationDTO.getAdminId()).orElseThrow(
                () -> new AdminException("Admin with id: " + notificationDTO.getAdminId() + " not found")
        );
        notificationModel.setAdmin(adminModel);
        NotificationModel savedNotification = notificationsRepository.save(notificationModel);
        logger.info("Notificacation created successfully with id: {}", savedNotification.getNotificationId());
        return notificationMapper.toDTO(savedNotification);

    }

    @Override
    public NotificationDTO updateNotification(Long notificationId, NotificationDTO notificationDTO) {
        NotificationModel notificationModel = notificationsRepository.findById(notificationId).orElseThrow(
                () -> new ResourceNotFoundException("Notification with id:  " + notificationId + " not found")
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
}
