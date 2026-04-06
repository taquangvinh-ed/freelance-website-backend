package com.freelancemarketplace.backend.freelancer.infrastructure.repository;

import com.freelancemarketplace.backend.freelancer.domain.model.ExperienceModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperiencesRepository extends JpaRepository<ExperienceModel, Long> {
  List<ExperienceModel>findAllByFreelancer(FreelancerModel freelancerModel);
  }