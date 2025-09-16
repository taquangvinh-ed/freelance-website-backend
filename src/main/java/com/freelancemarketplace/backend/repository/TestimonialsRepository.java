package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.TestimonialModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialsRepository extends JpaRepository<TestimonialModel, Long> {
  }