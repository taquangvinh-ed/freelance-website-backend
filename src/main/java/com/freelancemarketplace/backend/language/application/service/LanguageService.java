package com.freelancemarketplace.backend.language.application.service;

import com.freelancemarketplace.backend.language.dto.LanguageDTO;

import java.util.List;

public interface LanguageService {

    LanguageDTO createLanguage(LanguageDTO languageDTO);

    public Boolean deleteLanguages(Long languageId);

    public LanguageDTO updateLanguage(Long languageId, LanguageDTO languageDTO);

    public List<LanguageDTO>getAllLanguages();

}
