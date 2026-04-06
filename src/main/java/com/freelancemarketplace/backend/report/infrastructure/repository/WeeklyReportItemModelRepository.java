package com.freelancemarketplace.backend.report.infrastructure.repository;

import com.freelancemarketplace.backend.contract.domain.model.WeeklyReportItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyReportItemModelRepository extends JpaRepository<WeeklyReportItemModel, Long> {
}