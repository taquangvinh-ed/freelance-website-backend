package com.freelancemarketplace.backend.notification.api.controller;

import com.freelancemarketplace.backend.notification.dto.NotificationDTO;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.notification.application.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping("/notifications")
    ApiResponse<?> createNotification(@RequestBody NotificationDTO notificationDTO) {

        NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
        return ApiResponse.created(createdNotification);

    }

    @PutMapping("/notifications/{notificationId}")
    ApiResponse<?> updateNotification(@PathVariable Long notificationId,
                                                   @RequestBody NotificationDTO notificationDTO) {

        NotificationDTO updatedNotification = notificationService.updateNotification(notificationId, notificationDTO);
        logger.info("Notification with id: {} updated successfully", updatedNotification.getNotificationId());
        return ApiResponse.success(updatedNotification);

    }

    @DeleteMapping("/notifications/{notificationId}")
    ApiResponse<?> deleteNotification(@PathVariable Long notificationId) {

        notificationService.deleteNotification(notificationId);
        return ApiResponse.noContent();

    }

    @GetMapping("/notifications/getAll")
    ApiResponse<?> getAllNotifications() {

        List<NotificationDTO> notificationDTOs = notificationService.getAllNotification();
        return ApiResponse.success(notificationDTOs);

    }




}
