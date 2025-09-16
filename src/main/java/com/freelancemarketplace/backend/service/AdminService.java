package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminModel;

import java.util.Set;

public interface AdminService {
    public AdminModel createAdmin(AdminDTO adminDTO);

    AdminDTO update(Long adminId, AdminDTO adminDTO);

    public Boolean delete(Long adminId);

    AdminDTO getProfileAdmin(Long adminId);

    Set<AdminDTO>getAllAdmins();

}
