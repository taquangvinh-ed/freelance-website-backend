package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ReviewDTO;
import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ReviewTypes;
import com.freelancemarketplace.backend.enums.ReviewerRoles;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ReviewMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.TestomonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

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
            }
            if (ReviewerRoles.CLIENT.toString().equals(reviewDTO.getReviewerRole())) {
                if (reviewDTO.getClientId() != null) {
                    ClientModel client = clientsRepository.findById(reviewDTO.getClientId()).orElseThrow(
                            () -> new ResourceNotFoundException("Client with id: " + reviewDTO.getClientId() + " not found"));
                    newReview.setClient(client);
                }
                newReview.setReviewerRole(ReviewerRoles.CLIENT);
                newReview.setType(ReviewTypes.CLIENT_TO_FREELANCER);
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
