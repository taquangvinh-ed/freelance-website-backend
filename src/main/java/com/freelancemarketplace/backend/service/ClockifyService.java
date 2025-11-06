package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClockifyStartRequest;
import com.freelancemarketplace.backend.dto.ClockifyTimeEntryResponse;

public interface ClockifyService {

    public String getOrCreateFreelancerUser(Long freelancerId, String freelancerName, String email);



    public ClockifyTimeEntryResponse startTimeEntry(
            Long freelancerId,
            Long contractId, String description);

    public ClockifyTimeEntryResponse stopTimeEntry(
            Long freelancerId);

}
