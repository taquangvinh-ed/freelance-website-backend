package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.service.AdminService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        logger.info("Received request to create admin with username: {}", adminDTO.getUsername());
        AdminModel createdAdmin = adminService.createAdmin(adminDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO("CREATED", "CREATED ", "Successfully created admin account with admin_id: " + createdAdmin.getId()));
    }

    @PutMapping("/admin")
    public ResponseEntity<ResponseDTO> updateAdmin(@RequestBody @Valid AdminDTO adminDTO){
        logger.info("Receive request to update admin with username: " + adminDTO.getUsername());
        if(adminService.update(adminDTO)){
            logger.info("Admin account updated successfully!" );
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO("OK", "Admin account with username: "+ adminDTO.getUsername()+ " updated successfully!" ));
        }else{
            logger.info("Cannot update this admin account");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("INTERNAL_SERVER_ERROR", "Cannot update this admin account"));

        }




    }

    @DeleteMapping("/admin")
    public ResponseEntity<ResponseDTO>deleteAdmin(@RequestParam Long adminId){
        logger.info("Receive request to delete admin account with id: " + adminId);
        if (adminService.delete(adminId)){
            logger.info("Deleted successfully");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO("OK", "Delete admin account with id: " + adminId + " successfully"));
        }else{
            logger.error("Cannot delete this admin account ");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("INTERNAL_SERVER_ERROR", "Cannot delete admin account with id: " + adminId));
        }
    }
}
