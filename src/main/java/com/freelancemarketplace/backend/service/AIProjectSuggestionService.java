package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.project.dto.AiSuggestProjectResponseDTO;
import com.freelancemarketplace.backend.project.api.request.AiSuggestProjectRequest;

public interface AIProjectSuggestionService {
    AiSuggestProjectResponseDTO suggestProject(Long userId, AiSuggestProjectRequest request);
}

