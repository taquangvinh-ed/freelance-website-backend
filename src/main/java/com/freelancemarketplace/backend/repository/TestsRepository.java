package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.TestsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestsRepository extends JpaRepository<TestsModel, Long> {
  }