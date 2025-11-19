package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.mapper.AdminMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.request.DisableUserRequest;
import com.freelancemarketplace.backend.request.UserRequest;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.AdminService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    public AdminController(AdminService adminService, AdminMapper adminMapper) {
        this.adminService = adminService;
        this.adminMapper = adminMapper;
    }

//    @PostMapping("/admin")
//    public ResponseEntity<ResponseDTO> createAdmin(@RequestBody @Valid AdminDTO adminDTO) {
//        AdminModel createdAdmin = adminService.createAdmin(adminDTO);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(new ResponseDTO(ResponseStatusCode.CREATED, ResponseMessage.CREATED, adminMapper.toDTO(createdAdmin)));
//    }

    @PutMapping("/{adminId}")
    public ResponseEntity<ResponseDTO> updateAdmin(@PathVariable Long adminId,
                                                   @RequestBody @Valid AdminDTO adminDTO) {
        logger.info("Receive request to update admin with username: " + adminDTO.getUsername());

        AdminDTO updatedAdminDTO = adminService.update(adminId, adminDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, updatedAdminDTO));

    }

    @DeleteMapping("/admin/{adminId}")
    public ResponseEntity<ResponseDTO> deleteAdmin(@PathVariable Long adminId) {
        logger.info("Receive request to delete admin account with id: " + adminId);
        if (adminService.delete(adminId)) {
            logger.info("Deleted successfully");
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDTO(ResponseStatusCode.NO_CONTENT, ResponseMessage.NO_CONTENT));
        } else {
            logger.error("Cannot delete this admin account ");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("INTERNAL_SERVER_ERROR", "Cannot delete admin account with id: " + adminId));
        }
    }

    @GetMapping("/admin/{adminId}")
    ResponseEntity<ResponseDTO> getAdminProfile(@PathVariable Long adminId) {
        logger.info("Receive request to get admin profile with id: " + adminId);


        AdminDTO adminDTO = adminService.getAdminProfile(adminId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, adminDTO));

    }

    @GetMapping("admin/getAll")
    public ResponseEntity<ResponseDTO> getAllAdminAccount() {

        List<AdminDTO> adminDTOList = adminService.getAllAdmins();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, adminDTOList));

    }

    @GetMapping("/users/getAllUser")
    ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam (required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role) {

        UserRequest request = UserRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .status(status)
                .role(role)
                .build();

        Page<UserDTO> users = adminService.getAllUsers(request);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/users/{userId}/activate")
    ResponseEntity<UserDTO> activateUser(@PathVariable Long userId, @AuthenticationPrincipal AppUser appUser) throws BadRequestException {
        Long adminId = appUser.getId();
        UserDTO user = adminService.activateUser(userId, adminId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{userId}/disable")
    ResponseEntity<UserDTO> disableUser(@PathVariable Long userId,
                                        @AuthenticationPrincipal AppUser appUser,
                                        @RequestBody DisableUserRequest request) throws BadRequestException {
        Long adminId = appUser.getId();
        UserDTO user = adminService.disableUser(userId, adminId, request);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{userId}/ban")
    ResponseEntity<UserDTO> banUser(@PathVariable Long userId,
                                        @AuthenticationPrincipal AppUser appUser,
                                        @RequestBody DisableUserRequest request) throws BadRequestException {
        Long adminId = appUser.getId();
        UserDTO user = adminService.banUser(userId, adminId, request);
        return ResponseEntity.ok(user);
    }

}
