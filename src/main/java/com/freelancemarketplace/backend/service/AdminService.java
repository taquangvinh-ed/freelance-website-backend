package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.model.AdminModel;

public interface AdminService {
    public AdminModel createAdmin(AdminDTO adminDTO);

    public Boolean update(AdminDTO adminDTO);

    public Boolean delete(Long adminId);
}
