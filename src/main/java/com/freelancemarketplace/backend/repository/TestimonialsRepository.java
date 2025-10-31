package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonialsRepository extends JpaRepository<TestimonialModel, Long> {

  List<TestimonialModel>findAllByFreelancer(FreelancerModel freelancerModel);

  Page<TestimonialModel> findAllByFreelancer(FreelancerModel freelancerModel,Pageable pageable);

  Page<TestimonialModel> findAllByTeam(TeamModel teamModel, Pageable pageable);

  Page<TestimonialModel> findAllByClient(ClientModel clientModel, Pageable pageable);

  long countByFreelancer(FreelancerModel freelancerModel);

}