package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.NotificationDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.NotificationException;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.LanguageService;
import com.freelancemarketplace.backend.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<ResponseDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {

        NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        createdNotification
                ));

    }

    @PutMapping("/notifications/{notificationId}")
    ResponseEntity<ResponseDTO> updateNotification(@PathVariable Long notificationId,
                                                   @RequestBody NotificationDTO notificationDTO) {

        NotificationDTO updatedNotification = notificationService.updateNotification(notificationId, notificationDTO);
        logger.info("Notification with id: {} updated successfully", updatedNotification.getNotificationId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedNotification));

    }

    @DeleteMapping("/notifications/{notificationId}")
    ResponseEntity<ResponseDTO> deleteNotification(@PathVariable Long notificationId) {

        notificationService.deleteNotification(notificationId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));

    }

    @GetMapping("/notifications/getAll")
    ResponseEntity<ResponseDTO> getAllNotifications() {

        List<NotificationDTO> notificationDTOs = notificationService.getAllNotification();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        notificationDTOs
                ));

    }




}
