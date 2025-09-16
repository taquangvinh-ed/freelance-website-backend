package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.exception.LanguageException;
import com.freelancemarketplace.backend.mapper.LanguageMapper;
import com.freelancemarketplace.backend.model.LanguagesModel;
import com.freelancemarketplace.backend.repository.LanguagesRepository;
import com.freelancemarketplace.backend.service.LanguageService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    Logger logger = LoggerFactory.getLogger(LanguageServiceImpl.class);


    private final LanguagesRepository languagesRepository;
    private final LanguageMapper languageMapper;

    public LanguageServiceImpl(LanguagesRepository languagesRepository,
                               LanguageMapper languageMapper) {
        this.languagesRepository = languagesRepository;
        this.languageMapper = languageMapper;
    }

    @Override
    @Transactional
    public Long createLanguage(LanguageDTO languageDTO) {
        if (languagesRepository.existsByLanguageName(languageDTO.getLanguageName())) {
            logger.warn("This languge has already existed ");
            throw new LanguageException("This language has already existed;");
        }
        try {
            LanguagesModel newLanguage = new LanguagesModel();
            newLanguage = languageMapper.toEntity(languageDTO);
            newLanguage.setCreated_at(Timestamp.from(Instant.now()));
            LanguagesModel savedLanguage = languagesRepository.save(newLanguage);
            if (savedLanguage.getId() != null) {
                logger.info("Created language successfully!!!");
                return savedLanguage.getId();
            }
        } catch (DataAccessException e) {
            logger.error("Failed to create language with error: " + e);
            throw new LanguageException("Cannot create language");
        }

        return null;
    }

    @Override
    @Transactional
    public Boolean deleteLanguages(Long languageId) {
        boolean exists = languagesRepository.existsById(languageId);
        if (!exists) {
            return false;
        } else {
            languagesRepository.deleteById(languageId);
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean updateLanguage(Long languageId, LanguageDTO languageDTO) {
        Optional<LanguagesModel> language = languagesRepository.findById(languageId);
        if (language.isPresent()) {
            LanguagesModel existingLanguage = language.get();
            existingLanguage.setLanguageName(languageDTO.getLanguageName());
            existingLanguage.setUpdated_at(Timestamp.from(Instant.now()));
            try {
                LanguagesModel updatedLanguage = languagesRepository.save(existingLanguage);
                return true;
            } catch (DataAccessException e) {
                System.out.println("Failed to update language: " + e.getMessage());
                throw new RuntimeException("Failed to update language due to database error: " + e);
            }
        } else {
            return false;
        }
    }
}
