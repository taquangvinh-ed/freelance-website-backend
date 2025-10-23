package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import com.freelancemarketplace.backend.model.TeamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalsRepository extends JpaRepository<ProposalModel, Long> {
    List<ProposalModel> findAllByFreelancer(FreelancerModel freelancerModel);

    List<ProposalModel> getAllByTeam(TeamModel teamModel);

    List<ProposalModel> findAllByProject(ProjectModel project);

    ProposalModel findByFreelancerAndProject(FreelancerModel freelancer, ProjectModel project);

    @Modifying
    @Query("UPDATE ProposalModel p SET p.status = 'REJECTED' " +
            "WHERE p.project.projectId = :projectId AND p.proposalId != :approvedProposalId " +
            "AND p.status = 'PENDING'")
    void rejectOtherProposalsInProject(@Param("projectId") Long projectId, @Param("approvedProposalId") Long approvedProposalId);
  }