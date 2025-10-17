package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import com.freelancemarketplace.backend.model.TeamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalsRepository extends JpaRepository<ProposalModel, Long> {
    List<ProposalModel> findAllByFreelancer(FreelancerModel freelancerModel);

    List<ProposalModel> getAllByTeam(TeamModel teamModel);

    List<ProposalModel> findAllByProject(ProjectModel project);

    ProposalModel findByFreelancerAndProject(FreelancerModel freelancer, ProjectModel project);
  }