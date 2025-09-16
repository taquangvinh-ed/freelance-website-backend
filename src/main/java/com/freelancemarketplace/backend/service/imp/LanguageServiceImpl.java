package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.exception.LanguageException;
import com.freelancemarketplace.backend.mapper.LanguageMapper;
import com.freelancemarketplace.backend.model.LanguageModel;
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
            LanguageModel newLanguage = new LanguageModel();
            newLanguage = languageMapper.toEntity(languageDTO);
            newLanguage.setCreatedAt(Timestamp.from(Instant.now()));
            LanguageModel savedLanguage = languagesRepository.save(newLanguage);
            if (savedLanguage.getLanguageId() != null) {
                logger.info("Created language successfully!!!");
                return savedLanguage.getLanguageId();
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
        Optional<LanguageModel> language = languagesRepository.findById(languageId);
        if (language.isPresent()) {
            LanguageModel existingLanguage = language.get();
            existingLanguage.setLanguageName(languageDTO.getLanguageName());
            existingLanguage.setUpdatedAt(Timestamp.from(Instant.now()));
            try {
                LanguageModel updatedLanguage = languagesRepository.save(existingLanguage);
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
