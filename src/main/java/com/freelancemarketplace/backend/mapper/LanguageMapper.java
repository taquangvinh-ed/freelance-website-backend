package com.freelancemarketplace.backend.mapper;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.model.LanguageModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    LanguageDTO toDTO(LanguageModel language);

    LanguageModel toEntity(LanguageDTO languageDto);

    List<LanguageDTO> toDTOs(List<LanguageModel> languages);
}
