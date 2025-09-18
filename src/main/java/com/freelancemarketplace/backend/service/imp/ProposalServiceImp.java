package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProposalMapper;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.ProposalModel;
import com.freelancemarketplace.backend.model.TeamModel;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ProposalService;
import org.springframework.stereotype.Service;

@Service
public class ProposalServiceImp implements ProposalService {

    private final ProposalsRepository proposalsRepository;
    private final ProposalMapper proposalMapper;
    private final FreelancersRepository freelancersRepository;
    private final TeamsRepository teamsRepository;
    private final ProjectsRepository projectsRepository;

    public ProposalServiceImp(ProposalsRepository proposalsRepository, ProposalMapper proposalMapper, FreelancersRepository freelancersRepository, TeamsRepository teamsRepository, ProjectsRepository projectsRepository) {
        this.proposalsRepository = proposalsRepository;
        this.proposalMapper = proposalMapper;
        this.freelancersRepository = freelancersRepository;
        this.teamsRepository = teamsRepository;
        this.projectsRepository = projectsRepository;
    }

    @Override
    public ProposalDTO createProposal(ProposalDTO proposalDTO) {

        ProposalModel newProposal = proposalMapper.toEntity(proposalDTO);

        if(proposalDTO.getFreelancerId() != null){
            FreelancerModel freelancer = freelancersRepository.findById(proposalDTO.getFreelancerId()).orElseThrow(
                    ()->new ResourceNotFoundException("Freelancer id: " + proposalDTO.getFreelancerId() + "inside proposal creating request create not found")
            );
            newProposal.setFreelancer(freelancer);
        }

        if(proposalDTO.getTeamId() != null){
            TeamModel team = teamsRepository.findById(proposalDTO.getTeamId()).orElseThrow(
                    ()->new ResourceNotFoundException("Team id: " + proposalDTO.getTeamId() + "inside proposal creating request create not found")
            );
            newProposal.setTeam(team);
        }
            ProjectModel project = projectsRepository.findById(proposalDTO.getProjectId()).orElseThrow(
                    ()->new ResourceNotFoundException("Project id: " + proposalDTO.getProjectId() + "inside proposal creating request create not found")
            );
            newProposal.setProject(project);

            ProposalModel savedProposal = proposalsRepository.save(newProposal);


        return proposalMapper.toDto(savedProposal);
    }

    @Override
    public ProposalDTO updateProposal(Long proposalId, ProposalDTO proposalDTO) {
        return null;
    }

    @Override
    public ProposalDTO deleteProposal(Long proposalId) {
        return null;
    }
}
