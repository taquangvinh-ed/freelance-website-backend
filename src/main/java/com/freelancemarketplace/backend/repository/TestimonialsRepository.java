package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.TestimonialsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialsRepository extends JpaRepository<TestimonialsModel, Long> {
  }