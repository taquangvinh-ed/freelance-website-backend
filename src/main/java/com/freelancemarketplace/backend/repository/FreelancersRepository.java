package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancersRepository extends JpaRepository<FreelancerModel, Long> {
  }