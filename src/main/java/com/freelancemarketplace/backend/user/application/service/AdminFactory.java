package com.freelancemarketplace.backend.user.application.service;

import com.freelancemarketplace.backend.admin.domain.model.AdminModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import org.springframework.stereotype.Service;

@Service("adminFactory")
public class AdminFactory implements UserFactory<AdminModel> {

    @Override
    public AdminModel createProfile(RegistrationtDTO registrationDTO, UserModel savedUser) {
        AdminModel admin = new AdminModel();
        admin.setAdminId(savedUser.getUserId());
        admin.setFirstName(registrationDTO.getFirstName());
        admin.setLastName(registrationDTO.getLastName());
        admin.setUser(savedUser);
        return admin;
    }
}
