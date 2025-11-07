package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.WeeklyReportModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyReportModelRepository extends JpaRepository<WeeklyReportModel, Long> {
    List<WeeklyReportModel> findAllByContract(ContractModel contract);
}