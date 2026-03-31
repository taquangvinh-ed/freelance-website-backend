package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.domain.model.ProjectModel;
import com.freelancemarketplace.backend.domain.model.ProjectQuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectQuestionsRepository extends JpaRepository<ProjectQuestionModel, Long> {
    List<ProjectQuestionModel> findAllByProjectOrderByCreatedAtDesc(ProjectModel project);
}

