package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.domain.model.WeeklyReportItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyReportItemModelRepository extends JpaRepository<WeeklyReportItemModel, Long> {
}