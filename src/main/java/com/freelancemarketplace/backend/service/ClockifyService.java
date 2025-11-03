package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClockifyStartRequest;
import com.freelancemarketplace.backend.dto.ClockifyTimeEntryResponse;

public interface ClockifyService {

    public ClockifyTimeEntryResponse startTimeEntry(
            Long freelancerId,
            Long contractId,
            ClockifyStartRequest request);

    public ClockifyTimeEntryResponse stopTimeEntry(
            Long freelancerId);


}
