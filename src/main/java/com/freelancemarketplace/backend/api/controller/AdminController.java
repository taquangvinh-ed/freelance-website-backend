package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.request.DisableUserRequest;
import com.freelancemarketplace.backend.api.request.UserRequest;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;


    @PutMapping("/{adminId}")
    public ApiResponse<?> updateAdmin(@PathVariable Long adminId,
                                   @RequestBody @Valid AdminDTO adminDTO) {
        AdminDTO updatedAdminDTO = adminService.update(adminId, adminDTO);
        return ApiResponse.success(updatedAdminDTO);
    }

    @DeleteMapping("/admin/{adminId}")
    public ApiResponse<?> deleteAdmin(@PathVariable Long adminId) {
       adminService.delete(adminId);
        return ApiResponse.delete();
    }

    @GetMapping("/admin/{adminId}")
    public ApiResponse<?> getAdminProfile(@PathVariable Long adminId) {
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
    public ApiResponse<?> disableUser(@PathVariable Long userId,
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
