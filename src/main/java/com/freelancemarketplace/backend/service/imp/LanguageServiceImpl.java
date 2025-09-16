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

import java.util.List;

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
    public LanguageDTO createLanguage(LanguageDTO languageDTO) {
        if (languagesRepository.existsByIsoCode(languageDTO.getIsoCode())) {
            logger.warn("This languge has already existed ");
            throw new LanguageException("This language has already existed;");
        }
        try {
            LanguageModel newLanguage = new LanguageModel();
            newLanguage = languageMapper.toEntity(languageDTO);
            LanguageModel savedLanguage = languagesRepository.save(newLanguage);
            logger.info("Created language successfully!!!");
            return languageMapper.toDTO(savedLanguage);

        } catch (DataAccessException e) {
            logger.error("Failed to create language with error: " + e);
            throw new LanguageException("Cannot create language");
        }
    }

    @Override
    @Transactional
    public Boolean deleteLanguages(Long languageId) {
        try{
            languagesRepository.deleteById(languageId);
            logger.info("The language with id: {} is deleted", languageId);
            return true;
        }catch (DataAccessException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @Override
    @Transactional
    public LanguageDTO updateLanguage(Long languageId, LanguageDTO languageDTO) {
        LanguageModel language = languagesRepository.findById(languageId).orElseThrow(()->new LanguageException("Language with id:  " + languageId + " not found"));

        if(languageDTO.getLanguageName() != null
                    && !languageDTO.getLanguageName().equals(language.getLanguageName())
                    && languagesRepository.existsByLanguageName(languageDTO.getLanguageName()))
                throw new LanguageException("This iso code of language has already existed");
        if (languageDTO.getIsoCode() != null && !languageDTO.getIsoCode().equals(language.getIsoCode())
                && languagesRepository.existsByIsoCode(languageDTO.getIsoCode())) {
            throw new LanguageException("This ISO code has already existed.");
        }

            try {
                if(languageDTO.getLanguageName() != null)
                    language.setLanguageName(languageDTO.getLanguageName());
                if(languageDTO.getIsoCode() != null)
                    language.setIsoCode((languageDTO.getIsoCode()));
                if(languageDTO.getIsActived() != null)
                    language.setIsActived(languageDTO.getIsActived());
                languagesRepository.save(language);
                return languageMapper.toDTO(language);
            } catch (DataAccessException e) {
                System.out.println("Failed to update language: " + e.getMessage());
                throw new RuntimeException("Failed to update language due to database error: " + e);
            }

        }
    @Transactional
    @Override
    public List<LanguageDTO> getAllLanguages() {
        try {
            List<LanguageModel> languages = languagesRepository.findAll();
            return languageMapper.toDTOs(languages);
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e );
        }

    }
}

