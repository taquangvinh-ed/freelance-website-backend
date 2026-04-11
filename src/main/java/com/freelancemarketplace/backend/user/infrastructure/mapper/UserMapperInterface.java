package com.freelancemarketplace.backend.user.infrastructure.mapper;

import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;

public interface UserMapperInterface {

    UserModel registraionDtoToUserEntity(RegistrationtDTO registrationtDTO);
}
