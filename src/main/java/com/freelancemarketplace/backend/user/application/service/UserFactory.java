package com.freelancemarketplace.backend.user.application.service;

import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;

public interface UserFactory<T extends BaseEntity> {

    T createProfile(RegistrationtDTO registrationDTO, UserModel savedUser) throws Exception;
}
