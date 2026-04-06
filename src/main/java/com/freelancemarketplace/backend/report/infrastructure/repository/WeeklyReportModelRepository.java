package com.freelancemarketplace.backend.report.infrastructure.repository;

import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.contract.domain.model.WeeklyReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyReportModelRepository extends JpaRepository<WeeklyReportModel, Long> {
    List<WeeklyReportModel> findAllByContract(ContractModel contract);
}