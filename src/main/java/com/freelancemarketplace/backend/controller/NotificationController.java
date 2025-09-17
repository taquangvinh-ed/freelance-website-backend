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
    ResponseEntity<ResponseDTO> createNotification(@RequestBody NotificationDTO notificationDTO){
        try {
            NotificationDTO createdNotification = notificationService.createNotification(notificationDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.CREATED,
                            ResponseMessage.CREATED,
                            createdNotification
                    ));
        } catch (RuntimeException e) {
            throw new NotificationException("Message: "+e);
        }
    }

    @PutMapping("/notifications/{notificationId}")
    ResponseEntity<ResponseDTO>updateNotification(@PathVariable Long notificationId,
                                                  @RequestBody NotificationDTO notificationDTO){
        try{
            NotificationDTO updatedNotification = notificationService.updateNotification(notificationId, notificationDTO);
            logger.info("Notification with id: {} updated successfully", updatedNotification.getNotificationId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            updatedNotification));
        } catch (RuntimeException e) {
            throw new AdminException("Message: " + e);
        }
    }

    @DeleteMapping("/notifications/{notificationId}")
    ResponseEntity<ResponseDTO>deleteNotification(@PathVariable Long notificationId){
        try{
            notificationService.deleteNotification(notificationId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS
                    ));
        } catch (RuntimeException e) {
            throw new AdminException("Message: "+ e);
        }
    }

    @GetMapping("/notifications/getAll")
    ResponseEntity<ResponseDTO>getAllNotifications(){
        try{
            List<NotificationDTO> notificationDTOs = notificationService.getAllNotification();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            notificationDTOs
                    ));
        } catch (RuntimeException e) {
            throw new NotificationException("Error: ", e);
        }
    }

}
