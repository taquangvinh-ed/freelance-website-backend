package com.freelancemarketplace.backend.project.infrastructure.repository;

import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectQuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectQuestionsRepository extends JpaRepository<ProjectQuestionModel, Long> {
    List<ProjectQuestionModel> findAllByProjectOrderByCreatedAtDesc(ProjectModel project);
}

