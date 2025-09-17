package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.LanguageDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.exception.LanguageException;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.LanguageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<ResponseDTO> createLanguage(@RequestBody @Valid LanguageDTO languageDTO){
        try{
            LanguageDTO createdlanguage = languageService.createLanguage(languageDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.CREATED,
                            ResponseMessage.CREATED,
                            createdlanguage
                    ));
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @PutMapping("/languages/{languageId}")
    ResponseEntity<ResponseDTO>updateLanguage(@PathVariable Long languageId,
                                              @RequestBody LanguageDTO languageDTO){
        try{
            LanguageDTO updatedLanguage = languageService.updateLanguage(languageId, languageDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            updatedLanguage
                    ));
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @DeleteMapping("/languages/{languageId}")
    ResponseEntity<ResponseDTO>deleteLanguage(@PathVariable Long languageId){
        try{
            languageService.deleteLanguages(languageId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS
                    ));
        }catch (RuntimeException e){
            throw new LanguageException("Message: " + e);
        }
    }

    @GetMapping("/languages/getAll")
    ResponseEntity<ResponseDTO>getAllLanguages(){
        try{
            List<LanguageDTO> languageDTOs = languageService.getAllLanguages();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            languageDTOs
                    ));
        } catch (RuntimeException e) {
            throw new LanguageException("Message: " + e);
        }
    }

}
