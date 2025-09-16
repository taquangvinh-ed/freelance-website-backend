package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.LanguageDTO;

public interface LanguageService {

    LanguageDTO createLanguage(LanguageDTO languageDTO);

    public Boolean deleteLanguages(Long languageId);

    public LanguageDTO updateLanguage(Long languageId, LanguageDTO languageDTO);

}
