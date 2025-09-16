package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.NotificationDTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.NotificationException;
import com.freelancemarketplace.backend.mapper.NotificationMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.NotificationModel;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.repository.NotificationsRepository;
import com.freelancemarketplace.backend.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
        try{
            NotificationModel notificationModel = notificationMapper.toEntity(notificationDTO);

            AdminModel adminModel = adminsRepository.findById(notificationDTO.getAdminId()).orElseThrow(
                    ()->new AdminException("Admin with id: "+ notificationDTO.getAdminId() +" not found")
            );
            notificationModel.setAdmin(adminModel);
            NotificationModel savedNotification = notificationsRepository.save(notificationModel);
            logger.info("Notificacation created successfully with id: {}", savedNotification.getNotificationId());
            return notificationMapper.toDTO(savedNotification);
        } catch (DataAccessException e) {
            throw new NotificationException("Message: "+e);
        }
    }

    @Override
    public NotificationDTO updateNotification(Long notificationId, NotificationDTO notificationDTO) {
        return null;
    }

    @Override
    public Boolean deleteNotification(Long notificationId) {
        return null;
    }

    @Override
    public List<NotificationDTO> getAllNotification() {
        return List.of();
    }
}
