package com.freelancemarketplace.backend.admin.api.controller;

import com.freelancemarketplace.backend.admin.application.service.AdminService;
import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.admin.dto.AdminDTO;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.user.dto.UserDTO;
import com.freelancemarketplace.backend.user.api.request.DisableUserRequest;
import com.freelancemarketplace.backend.user.api.request.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;

    @PutMapping("/{adminId}")
    public ApiResponse<?> updateAdmin(@PathVariable Long adminId,
                                   @RequestBody @Valid AdminDTO adminDTO) {
        AdminDTO updatedAdminDTO = adminService.update(adminId, adminDTO);
        return ApiResponse.success(updatedAdminDTO);

    }

    @DeleteMapping("/admin/{adminId}")
    public ApiResponse<?> deleteAdmin(@PathVariable Long adminId) {
        logger.info("Receive request to delete admin account with id: " + adminId);
        adminService.delete(adminId);
        logger.info("Deleted successfully");
        return ApiResponse.noContent();
    }

    @GetMapping("/admin/{adminId}")
    ApiResponse<?> getAdminProfile(@PathVariable Long adminId) {
        logger.info("Receive request to get admin profile with id: " + adminId);
        AdminDTO adminDTO = adminService.getAdminProfile(adminId);
        return ApiResponse.success(adminDTO);
    }

    @GetMapping("admin/getAll")
    public ApiResponse<?> getAllAdminAccount() {
        List<AdminDTO> adminDTOList = adminService.getAllAdmins();
        return ApiResponse.success(adminDTOList);
    }

    @GetMapping("/users/getAllUser")
    ApiResponse<?> getAllUsers(
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
        return ApiResponse.success(users);
    }

    @PatchMapping("/users/{userId}/activate")
    ApiResponse<?> activateUser(@PathVariable Long userId, @AuthenticationPrincipal AppUser appUser) throws BadRequestException {
        Long adminId = appUser.getId();
        UserDTO user = adminService.activateUser(userId, adminId);
        return ApiResponse.success(user);
    }

    @PatchMapping("/users/{userId}/disable")
    ApiResponse<?> disableUser(@PathVariable Long userId,
                                        @AuthenticationPrincipal AppUser appUser,
                                        @RequestBody DisableUserRequest request) throws BadRequestException {
        Long adminId = appUser.getId();
        UserDTO user = adminService.disableUser(userId, adminId, request);
        return ApiResponse.success(user);
    }

    @PatchMapping("/users/{userId}/ban")
    ApiResponse<?> banUser(@PathVariable Long userId,
                                        @AuthenticationPrincipal AppUser appUser,
                                        @RequestBody DisableUserRequest request) throws BadRequestException {
        Long adminId = appUser.getId();
        UserDTO user = adminService.banUser(userId, adminId, request);
        return ApiResponse.success(user);
    }

}
