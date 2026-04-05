package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancersRepository extends JpaRepository<FreelancerModel, Long> {
  Optional<FreelancerModel> findByStripeAccountId(String stripeAccountId);
  }