package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.enums.AccountStatus;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.request.DisableUserRequest;
import com.freelancemarketplace.backend.request.UserRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
//    public AdminModel createAdmin(AdminDTO adminDTO);

    Page<UserDTO> getAllUsers(UserRequest request);

    UserDTO activateUser(Long userId, Long currentAdminId) throws BadRequestException;

    UserDTO updateUserStatus(Long userId, Long currentAdminId, AccountStatus status, String reason) throws BadRequestException;

    UserDTO disableUser(Long userId, Long currentAdminId, DisableUserRequest request) throws BadRequestException;

    // 4. Cấm vĩnh viễn (Ban)
    UserDTO banUser(Long userId, Long currentAdminId, DisableUserRequest request) throws BadRequestException;

    AdminDTO update(Long adminId, AdminDTO adminDTO);

    public Boolean delete(Long adminId);

    AdminDTO getAdminProfile(Long adminId);

    List<AdminDTO> getAllAdmins();

}
