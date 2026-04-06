package com.freelancemarketplace.backend.review.application.service.imp;

import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;

import com.freelancemarketplace.backend.review.dto.ReviewDTO;
import com.freelancemarketplace.backend.contract.domain.enums.ContractStatus;
import com.freelancemarketplace.backend.review.domain.enums.ReviewTypes;
import com.freelancemarketplace.backend.review.domain.enums.ReviewerRoles;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.contract.infrastructure.repository.ContractsRepository;
import com.freelancemarketplace.backend.review.infrastructure.mapper.ReviewMapper;
import com.freelancemarketplace.backend.review.infrastructure.repository.TestimonialsRepository;
import com.freelancemarketplace.backend.review.application.service.TestomonialService;
import com.freelancemarketplace.backend.team.infrastructure.repository.TeamsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.review.domain.model.TestimonialModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;

@Service
@RequiredArgsConstructor
public class TestimonialServiceImp implements TestomonialService {

    private final TestimonialsRepository testimonialsRepository;
    private final ReviewMapper reviewMapper;
    private final FreelancersRepository freelancersRepository;
    private final TeamsRepository teamsRepository;
    private final ClientsRepository clientsRepository;
    private final ContractsRepository contractsRepository;

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {

        ContractModel contract = contractsRepository.findById(reviewDTO.getContractId()).orElseThrow(
                () -> new ResourceNotFoundException("contract with id: " + reviewDTO.getContractId() + " not found"));


        if (!ContractStatus.ACTIVE.equals(contract.getStatus())) {
            throw new IllegalStateException("Can only review completed contracts");
        }


        TestimonialModel newReview = reviewMapper.toEntity(reviewDTO);
        newReview.setDatePosted(Timestamp.from(Instant.now()));


        if (reviewDTO.getReviewerRole() != null) {
            if (ReviewerRoles.FREELANCER.toString().equals(reviewDTO.getReviewerRole())) {
                if (reviewDTO.getFreelancerId() != null) {
                    FreelancerModel freelancer = freelancersRepository.findById(reviewDTO.getFreelancerId()).orElseThrow(
                            () -> new ResourceNotFoundException("Freelancer with id: " + reviewDTO.getFreelancerId() + " not found"));
                    newReview.setFreelancer(freelancer);
                }
                newReview.setReviewerRole(ReviewerRoles.FREELANCER);
                newReview.setType(ReviewTypes.FREELANCER_TO_CLIENT);
                newReview.setClient(contract.getClient());
            }
            if (ReviewerRoles.CLIENT.toString().equals(reviewDTO.getReviewerRole())) {
                if (reviewDTO.getClientId() != null) {
                    ClientModel client = clientsRepository.findById(reviewDTO.getClientId()).orElseThrow(
                            () -> new ResourceNotFoundException("Client with id: " + reviewDTO.getClientId() + " not found"));
                    newReview.setClient(client);
                }
                newReview.setReviewerRole(ReviewerRoles.CLIENT);
                newReview.setType(ReviewTypes.CLIENT_TO_FREELANCER);
                newReview.setFreelancer(contract.getFreelancer());
            }

        }


        if (reviewDTO.getTeamId() != null) {
            TeamModel team = teamsRepository.findById(reviewDTO.getTeamId()).orElseThrow(
                    () -> new ResourceNotFoundException("Team with id: " + reviewDTO.getTeamId() + " not found"));
            newReview.setTeam(team);
        }


        if (reviewDTO.getContractId() != null) {

            ProjectModel project = contract.getContractProject();
            newReview.setProject(project);
        }

        TestimonialModel savedReview = testimonialsRepository.save(newReview);

        return reviewMapper.toDto(savedReview);
    }


    @Override
    public void deleteReview(Long testimonialId) {
        if (!testimonialsRepository.existsById(testimonialId))
            throw new ResourceNotFoundException("Review with id: " + testimonialId + " not found");

        testimonialsRepository.deleteById(testimonialId);
    }

    @Override
    public Page<ReviewDTO> getAllReviewByFreelancer(Long freelancerId, Pageable pageable) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found"));

        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByFreelancer(freelancer, pageable);
        return reviewMapper.toDTOPage(testimonialModelPage, pageable);
    }

    @Override
    public Page<ReviewDTO> getAllReviewByTeam(Long teamId, Pageable pageable) {
        TeamModel team = teamsRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id: " + teamId + " not found"));
        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByTeam(team, pageable);
        return reviewMapper.toDTOPage(testimonialModelPage, pageable);
    }

    @Override
    public Page<ReviewDTO> getAllReviewByClient(Long clientId, Pageable pageable) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("Client with id: " + clientId + " not found"));
        Page<TestimonialModel> testimonialModelPage = testimonialsRepository.findAllByClient(client, pageable);

        return reviewMapper.toDTOPage(testimonialModelPage, pageable);
    }

}
