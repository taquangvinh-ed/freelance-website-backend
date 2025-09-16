package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.LanguageDTO;

public interface LanguageService {

    Long createLanguage(LanguageDTO languageDTO);

    public Boolean deleteLanguages(Long languageId);

    public Boolean updateLanguage(Long languageId, LanguageDTO languageDTO);

}
