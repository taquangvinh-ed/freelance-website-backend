package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.dto.NotificationDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.LanguageException;
import com.freelancemarketplace.backend.exception.NotificationException;
import com.freelancemarketplace.backend.mapper.AdminMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.AdminService;
import com.freelancemarketplace.backend.service.LanguageService;
import com.freelancemarketplace.backend.service.NotificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final AdminMapper adminMapper;
    private final LanguageService languageService;
    private final NotificationService notificationService;

    public AdminController(AdminService adminService,
                           AdminMapper adminMapper,
                           LanguageService languageService,
                           NotificationService notificationService) {
        this.adminService = adminService;
        this.adminMapper = adminMapper;
        this.languageService = languageService;
        this.notificationService = notificationService;
    }


    @PostMapping("/admin")
    public ResponseEntity<ResponseDTO> createAdmin(@RequestBody @Valid AdminDTO adminDTO) {
        logger.info("Received request to create admin with username: {}", adminDTO.getUsername());
        AdminModel createdAdmin = adminService.createAdmin(adminDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(ResponseStatusCode.CREATED, ResponseMessage.CREATED, adminMapper.toDTO(createdAdmin)));
    }

    @PutMapping("/admin/{adminId}")
    public ResponseEntity<ResponseDTO> updateAdmin(@PathVariable Long adminId,
                                                   @RequestBody @Valid AdminDTO adminDTO) {
        logger.info("Receive request to update admin with username: " + adminDTO.getAdminId());
        try {
            AdminDTO updatedAdminDTO = adminService.update(adminId, adminDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, updatedAdminDTO));
        } catch (RuntimeException e) {

            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/admin/{adminId}")
    public ResponseEntity<ResponseDTO> deleteAdmin(@PathVariable Long adminId) {
        logger.info("Receive request to delete admin account with id: " + adminId);
        if (adminService.delete(adminId)) {
            logger.info("Deleted successfully");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS));
        } else {
            logger.error("Cannot delete this admin account ");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("INTERNAL_SERVER_ERROR", "Cannot delete admin account with id: " + adminId));
        }
    }

    @GetMapping("/admin/{adminId}")
    ResponseEntity<ResponseDTO> getAdminProfile(@PathVariable Long adminId){
        logger.info("Receive request to get admin profile with id: " + adminId);

        try {
            AdminDTO adminDTO = adminService.getAdminProfile(adminId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, adminDTO));
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("admin/getAll")
    public ResponseEntity<ResponseDTO> getAllAdminAccount(){
        try{
            List<AdminDTO> adminDTOList = adminService.getAllAdmins();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, adminDTOList));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/admin/languages")
    ResponseEntity<ResponseDTO>createLanguage(@RequestBody @Valid LanguageDTO languageDTO){
        try{
            LanguageDTO createdlanguage = languageService.createLanguage(languageDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.CREATED,
                            ResponseMessage.CREATED,
                            createdlanguage
                    ));
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @PutMapping("/admin/languages/{languageId}")
    ResponseEntity<ResponseDTO>updateLanguage(@PathVariable Long languageId,
            @RequestBody LanguageDTO languageDTO){
        try{
            LanguageDTO updatedLanguage = languageService.updateLanguage(languageId, languageDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            updatedLanguage
                    ));
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @DeleteMapping("/admin/languages/{languageId}")
    ResponseEntity<ResponseDTO>deleteLanguage(@PathVariable Long languageId){
        try{
            languageService.deleteLanguages(languageId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS
                    ));
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @GetMapping("/admin/languages/getAll")
    ResponseEntity<ResponseDTO>getAllLanguages(){
        try{
            List<LanguageDTO> languageDTOs = languageService.getAllLanguages();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            languageDTOs
                    ));
        } catch (RuntimeException e) {
            throw new LanguageException("Message: " + e);
        }
    }

    @PostMapping("/admin/notifications")
    ResponseEntity<ResponseDTO>createNotification(@RequestBody NotificationDTO notificationDTO){
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

    @PutMapping("/admin/notifications/{notificationId}")
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

    @DeleteMapping("/admin/notifications/{notificationId}")
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

    @GetMapping("/admin/notifications/getAll")
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
