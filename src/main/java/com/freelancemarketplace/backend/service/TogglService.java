package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.TogglTimeEntryResponseDTO;
import jakarta.transaction.Transactional;

public interface TogglService {

    String getOrCreateFreelancerUser(Long freelancerId, String freelancerName, String email);

    Long createProjectOnToggl(String projectName);

    TogglTimeEntryResponseDTO startTimeEntry( // Trả về DTO phản hồi Toggl
                                              Long freelancerId,
                                              Long contractId,
                                              String description);

    @Transactional
    TogglTimeEntryResponseDTO stopTimeEntry(Long freelancerId);
}
