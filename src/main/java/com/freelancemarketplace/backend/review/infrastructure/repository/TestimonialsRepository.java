package com.freelancemarketplace.backend.review.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.review.domain.model.TestimonialModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;

@Repository
public interface TestimonialsRepository extends JpaRepository<TestimonialModel, Long> {

  List<TestimonialModel>findAllByFreelancer(FreelancerModel freelancerModel);

  Page<TestimonialModel> findAllByFreelancer(FreelancerModel freelancerModel,Pageable pageable);

  Page<TestimonialModel> findAllByTeam(TeamModel teamModel, Pageable pageable);

  Page<TestimonialModel> findAllByClient(ClientModel clientModel, Pageable pageable);

  long countByFreelancer(FreelancerModel freelancerModel);

}