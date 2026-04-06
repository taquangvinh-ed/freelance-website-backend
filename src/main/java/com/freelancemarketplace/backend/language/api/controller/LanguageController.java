package com.freelancemarketplace.backend.language.api.controller;

import com.freelancemarketplace.backend.language.dto.LanguageDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.language.exception.LanguageException;
import com.freelancemarketplace.backend.language.application.service.LanguageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin", produces = {MediaType.APPLICATION_JSON_VALUE})
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    Logger logger = LoggerFactory.getLogger(LanguageController.class);

    @PostMapping("/languages")
    ApiResponse<?> createLanguage(@RequestBody @Valid LanguageDTO languageDTO) {

        LanguageDTO createdlanguage = languageService.createLanguage(languageDTO);
        return ApiResponse.created(createdlanguage);

    }

    @PutMapping("/languages/{languageId}")
    ApiResponse<?> updateLanguage(@PathVariable Long languageId,
                                               @RequestBody LanguageDTO languageDTO) {
        try {
            LanguageDTO updatedLanguage = languageService.updateLanguage(languageId, languageDTO);
            return ApiResponse.success(updatedLanguage);
        } catch (RuntimeException e) {
            throw new LanguageException("Message: " + e);
        }
    }

    @DeleteMapping("/languages/{languageId}")
    ApiResponse<?> deleteLanguage(@PathVariable Long languageId) {

        languageService.deleteLanguages(languageId);
        return ApiResponse.noContent();

    }

    @GetMapping("/languages/getAll")
    ApiResponse<?> getAllLanguages() {
        List<LanguageDTO> languageDTOs = languageService.getAllLanguages();
        return ApiResponse.success(languageDTOs);

    }

}
