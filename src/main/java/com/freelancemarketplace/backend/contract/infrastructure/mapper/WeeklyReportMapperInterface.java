package com.freelancemarketplace.backend.contract.infrastructure.mapper;

import com.freelancemarketplace.backend.contract.dto.WeeklyReportDTO;
import com.freelancemarketplace.backend.contract.domain.model.WeeklyReportModel;

import java.util.List;

public interface WeeklyReportMapperInterface {

    WeeklyReportDTO toDto(WeeklyReportModel weeklyReportModel);

    List<WeeklyReportDTO> toDTOs(List<WeeklyReportModel> weeklyReportModels);
}
