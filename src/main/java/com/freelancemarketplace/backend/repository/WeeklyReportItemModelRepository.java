package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.WeeklyReportItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyReportItemModelRepository extends JpaRepository<WeeklyReportItemModel, Long> {
}