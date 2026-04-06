package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.contract.dto.WeeklyReportDTO;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

public interface ContractReportingService {

    public WeeklyReportDTO generateWeeklyReport(Long contractId, Instant startTime, Instant endTime);

    ZoneId getZoneId();

    List<WeeklyReportDTO> getAllLogs(Long contractId);
}
