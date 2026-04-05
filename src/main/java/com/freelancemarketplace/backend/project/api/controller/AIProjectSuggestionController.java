package com.freelancemarketplace.backend.project.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.project.dto.AiSuggestProjectResponseDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.project.api.request.AiSuggestProjectRequest;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.project.application.service.AIProjectSuggestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public ApiResponse<AiSuggestProjectResponseDTO> suggestProject(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @RequestBody AiSuggestProjectRequest request) {

        if (appUser == null) {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage(), null);
        }

        try {
            AiSuggestProjectResponseDTO response = aiProjectSuggestionService.suggestProject(appUser.getId(), request);
            return ApiResponse.success(response);
        } catch (IllegalStateException ex) {
            return ApiResponse.error(ErrorCode.RATE_LIMIT_EXCEEDED.getCode(), ex.getMessage(), null);
        } catch (IllegalArgumentException ex) {
            return ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), ex.getMessage(), null);
        } catch (Exception ex) {
            return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    "Failed to generate AI suggestions: " + ex.getMessage(), null);
        }
    }
}

