package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.model.AdminsModel;
import com.freelancemarketplace.backend.service.AdminService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("/admin")
    public ResponseEntity<ResponseDTO> createAdmin(@RequestBody @Valid  AdminDTO adminDTO){
        logger.info("Received request to create admin with email: {}", adminDTO.getEmail());
        AdminsModel createdAdmin = adminService.createAdmin(adminDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO("CREATED", "CREATED ", "Successfully created admin account with admin_id: " + createdAdmin.getId()));
    }
}
