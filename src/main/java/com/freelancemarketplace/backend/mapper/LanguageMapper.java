package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.model.LanguagesModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    LanguageMapper INSTANCE = Mappers.getMapper(LanguageMapper.class);

    LanguageDTO toDTO(LanguagesModel language);

    LanguagesModel toEntity(LanguageDTO languageDto);
}
