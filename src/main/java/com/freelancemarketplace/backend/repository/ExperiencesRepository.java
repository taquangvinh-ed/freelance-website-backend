package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.ExperienceModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperiencesRepository extends JpaRepository<ExperienceModel, Long> {
  List<ExperienceModel>findAllByFreelancer(FreelancerModel freelancerModel);
  }