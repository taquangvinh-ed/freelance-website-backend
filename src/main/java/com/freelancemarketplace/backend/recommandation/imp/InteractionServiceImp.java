package com.freelancemarketplace.backend.recommandation.imp;

import com.freelancemarketplace.backend.recommendation.domain.enums.InteractionType;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import com.freelancemarketplace.backend.recommandation.InteractionService;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectInteractionModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.recommendation.infrastructure.repository.ProjectInteractionModelRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@AllArgsConstructor
public class InteractionServiceImp implements InteractionService {

    private final FreelancersRepository freelancersRepository;
    private final ProjectsRepository projectsRepository;
    private final ProjectInteractionModelRepository projectInteractionModelRepository;

    @Override
    public void logInteraction(Long freelancerId, Long projectId, InteractionType interactionType) {
        ProjectInteractionModel newInteraction = new ProjectInteractionModel();

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()-> new ResourceNotFoundException("Freelancer with id: "+ freelancerId+ " not found")
        );

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                ()->new ResourceNotFoundException("Project with id: " + projectId +" not found")
        );

        int score = switch (interactionType){
            case WON -> 5;
            case BIDDED -> 3;
            case SAVED, VIEWED -> 1;
            default -> 0;
        };

        newInteraction.setFreelancer(freelancer);
        newInteraction.setProject(project);
        newInteraction.setInteractionType(interactionType);
        newInteraction.setInteractionScore(score);
        newInteraction.setTimestamp(Timestamp.from(Instant.now()));
        newInteraction.setPositive(interactionType == InteractionType.WON);
        projectInteractionModelRepository.save(newInteraction);
    }
}
