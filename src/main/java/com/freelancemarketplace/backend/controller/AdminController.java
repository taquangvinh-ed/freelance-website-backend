package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.exception.LanguageException;
import com.freelancemarketplace.backend.mapper.AdminMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.AdminService;
import com.freelancemarketplace.backend.service.LanguageService;
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

    private AdminService adminService;
    private AdminMapper adminMapper;
    private LanguageService languageService;

    public AdminController(AdminService adminService,
                           AdminMapper adminMapper,
                           LanguageService languageService) {
        this.adminService = adminService;
        this.adminMapper = adminMapper;
        this.languageService = languageService;
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
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
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
}
