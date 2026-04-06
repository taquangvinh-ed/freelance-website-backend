package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.project.dto.AiSuggestProjectResponseDTO;
import com.freelancemarketplace.backend.api.response.ResponseDTO;
import com.freelancemarketplace.backend.project.api.request.AiSuggestProjectRequest;
import com.freelancemarketplace.backend.api.response.ResponseMessage;
import com.freelancemarketplace.backend.api.response.ResponseStatusCode;
import com.freelancemarketplace.backend.project.application.service.AIProjectSuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AIProjectSuggestionController {

    private final AIProjectSuggestionService aiProjectSuggestionService;

    @PostMapping("/ai-suggest")
    public ResponseEntity<ResponseDTO<AiSuggestProjectResponseDTO>> suggestProject(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @RequestBody AiSuggestProjectRequest request) {

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO<>(ResponseStatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED));
        }

        try {
            AiSuggestProjectResponseDTO response = aiProjectSuggestionService.suggestProject(appUser.getId(), request);
            return ResponseEntity.ok(new ResponseDTO<>(
                    ResponseStatusCode.SUCCESS,
                    "AI suggestion generated successfully",
                    response
            ));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ResponseDTO<>("429", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(ResponseStatusCode.BAD_REQUEST, ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO<>(ResponseStatusCode.INTERNAL_SERVER_ERROR,
                            "Failed to generate AI suggestions: " + ex.getMessage()));
        }
    }
}

