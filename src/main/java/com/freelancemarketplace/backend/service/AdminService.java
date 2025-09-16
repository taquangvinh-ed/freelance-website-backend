package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminModel;

import java.util.List;

public interface AdminService {
    public AdminModel createAdmin(AdminDTO adminDTO);

    AdminDTO update(Long adminId, AdminDTO adminDTO);

    public Boolean delete(Long adminId);

    AdminDTO getAdminProfile(Long adminId);

    List<AdminDTO> getAllAdmins();

}
