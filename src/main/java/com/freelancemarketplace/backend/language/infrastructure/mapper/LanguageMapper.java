package com.freelancemarketplace.backend.language.infrastructure.mapper;

import com.freelancemarketplace.backend.language.dto.LanguageDTO;
import com.freelancemarketplace.backend.language.domain.model.LanguageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    LanguageDTO toDTO(LanguageModel language);


    LanguageModel toEntity(LanguageDTO languageDto);

    List<LanguageDTO> toDTOs(List<LanguageModel> languages);
}
