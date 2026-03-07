package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AiSuggestProjectResponseDTO;
import com.freelancemarketplace.backend.request.AiSuggestProjectRequest;

public interface AIProjectSuggestionService {
    AiSuggestProjectResponseDTO suggestProject(Long userId, AiSuggestProjectRequest request);
}

