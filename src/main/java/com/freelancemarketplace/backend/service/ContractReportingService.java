package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.WeeklyReportResponseDTO;

import java.time.ZoneId;

public interface ContractReportingService {

    public WeeklyReportResponseDTO generateWeeklyReport(Long contractId);

    ZoneId getZoneId();
}
