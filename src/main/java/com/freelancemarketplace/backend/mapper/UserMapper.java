package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.model.UserModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "passwordHash", ignore = true)
    UserModel toEntity(UserDTO userDTO);

    UserModel registraionDtoToUserEntity(RegistrationtDTO registrationtDTO);

    RegistrationtDTO userEntityToRegistrationDTO(UserModel userModel);

    UserDTO toDto(UserModel userModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserModel partialUpdate(UserDTO userDTO, @MappingTarget UserModel userModel);
}