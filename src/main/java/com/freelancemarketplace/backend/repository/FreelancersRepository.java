package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.FreelancersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancersRepository extends JpaRepository<FreelancersModel, Long> {
  }