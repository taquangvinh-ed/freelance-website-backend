package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.model.LanguagesModel;
import com.freelancemarketplace.backend.repository.LanguagesRepository;
import com.freelancemarketplace.backend.service.LanguageService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguagesRepository languagesRepository;

    public LanguageServiceImpl(LanguagesRepository languagesRepository) {
        this.languagesRepository = languagesRepository;
    }

    @Override
    @Transactional
    public void createLanguage(LanguageDTO languageDTO) {
        LanguagesModel newLanguage = new LanguagesModel();
        newLanguage.setLanguage_name(languageDTO.getLanguageName());
        newLanguage.setCreated_at(Timestamp.from(Instant.now()));
        LanguagesModel savedLanguage =  languagesRepository.save(newLanguage);
        if(savedLanguage == null){
            throw new RuntimeException("Failed to create languageDTO");
        }

    }

    @Override
    @Transactional
    public Boolean deleteLanguages(Long languageId) {
        boolean exists = languagesRepository.existsById(languageId);
        if (!exists) {
            return false;
    }else{
            languagesRepository.deleteById(languageId);
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean updateLanguage (Long languageId, LanguageDTO languageDTO) {
        Optional<LanguagesModel> language = languagesRepository.findById(languageId);
        if(language.isPresent()){
            LanguagesModel existingLanguage = language.get();
            existingLanguage.setLanguage_name(languageDTO.getLanguageName());
            existingLanguage.setUpdated_at(Timestamp.from(Instant.now()));
            try{
                LanguagesModel updatedLanguage = languagesRepository.save(existingLanguage);
                return true;
            } catch (DataAccessException e) {
                System.out.println("Failed to update language: " + e.getMessage());
                throw new RuntimeException("Failed to update language due to database error: "+e);
            }
    }else {
            return false;
        }
}}
