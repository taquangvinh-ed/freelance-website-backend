package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialsRepository extends JpaRepository<TestimonialModel, Long> {

  Page<TestimonialModel> findAllByFreelancer(FreelancerModel freelancerModel,Pageable pageable);

  Page<TestimonialModel> findAllByTeam(TeamModel teamModel, Pageable pageable);

  Page<TestimonialModel> findAllByClient(ClientModel clientModel, Pageable pageable);

  Page<TestimonialModel> findAllByCompany(CompanyModel companyModel, Pageable pageable);

  Page<TestimonialModel> findAllByProject(ProjectModel projectModel,Pageable pageable);

  long countByFreelancer(FreelancerModel freelancerModel);

}