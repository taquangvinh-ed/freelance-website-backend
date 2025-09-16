package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.LanguageDTO;

import java.util.List;

public interface LanguageService {

    LanguageDTO createLanguage(LanguageDTO languageDTO);

    public Boolean deleteLanguages(Long languageId);

    public LanguageDTO updateLanguage(Long languageId, LanguageDTO languageDTO);

    public List<LanguageDTO>getAllLanguages();

}
