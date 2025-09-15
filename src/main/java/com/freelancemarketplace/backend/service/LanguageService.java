package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.model.LanguagesModel;

public interface LanguageService {

    void createLanguage(LanguageDTO languageDTO);

    public Boolean deleteLanguages(Long languageId);

    public Boolean updateLanguage(Long languageId, LanguageDTO languageDTO);

}
