package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.AnswerQuestionRequest;
import com.freelancemarketplace.backend.dto.CreateQuestionRequest;
import com.freelancemarketplace.backend.dto.ProjectQuestionDTO;
import com.freelancemarketplace.backend.enums.QuestionStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProjectQuestionMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.ProjectQuestionsRepository;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.service.ProjectQuestionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectQuestionServiceImp implements ProjectQuestionService {

    private final ProjectQuestionsRepository projectQuestionsRepository;
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;
    private final ProjectQuestionMapper projectQuestionMapper;
    private final FreelancersRepository freelancersRepository;

    @Override
    @Transactional
    public ProjectQuestionDTO createQuestion(Long userId, Long projectId, CreateQuestionRequest request) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        UserModel user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + userId + " not found.")
        );

        Optional<FreelancerModel> freelancerModel = freelancersRepository.findById(userId);

        // Optional: restrict to freelancer role only
        // if (user.getRole() != com.freelancemarketplace.backend.enums.UserRoles.FREELANCER) {
        //     throw new AccessDeniedException("Only freelancers can ask clarification questions on projects.");
        // }

        ProjectQuestionModel question = new ProjectQuestionModel();
        question.setProject(project);
        question.setQuestionText(request.getQuestionText());
        question.setAskedBy(user);
        question.setStatus(QuestionStatus.OPEN);
        question.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        ProjectQuestionModel saved = projectQuestionsRepository.save(question);
        ProjectQuestionDTO dto = projectQuestionMapper.toDto(saved);
        if (freelancerModel.isPresent()) {
            dto.setAskedByFirstName(freelancerModel.get().getFirstName());
            dto.setAskedByLastName(freelancerModel.get().getLastName());
            dto.setAskedByAvatar(freelancerModel.get().getAvatar());
        }
        return dto;
    }

    @Override
    @Transactional
    public ProjectQuestionDTO answerQuestion(Long userId, Long projectId, Long questionId, AnswerQuestionRequest request) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectQuestionModel question = projectQuestionsRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question with id: " + questionId + " not found.")
        );

        if (!question.getProject().getProjectId().equals(project.getProjectId())) {
            throw new ResourceNotFoundException("Question does not belong to project");
        }

        // Check ownership: project.client.user.userId must equal userId
        UserModel clientUser = project.getClient() != null && project.getClient().getUser() != null
                ? project.getClient().getUser()
                : null;

        ClientModel client = project.getClient();
        if (clientUser == null || !clientUser.getUserId().equals(userId)) {
            throw new AccessDeniedException("Only project owner can answer clarification questions.");
        }

        if (question.getStatus() != QuestionStatus.OPEN) {
            throw new IllegalStateException("Question is not open for answer.");
        }

        UserModel answerer = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + userId + " not found.")
        );

        question.setAnswerText(request.getAnswerText());
        question.setAnsweredBy(answerer);
        question.setAnsweredAt(new Timestamp(System.currentTimeMillis()));
        question.setStatus(QuestionStatus.ANSWERED);

        ProjectQuestionModel saved = projectQuestionsRepository.save(question);
        ProjectQuestionDTO dto = projectQuestionMapper.toDto(saved);
        dto.setAnsweredByFirstName(client.getFirstName());
        dto.setAnsweredByLastName(client.getLastName());
        dto.setAnsweredByAvatar(client.getAvatar());
        return dto;
    }

    @Override
    public List<ProjectQuestionDTO> getQuestionsByProject(Long projectId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        List<ProjectQuestionModel> list = projectQuestionsRepository.findAllByProjectOrderByCreatedAtDesc(project);
        List<ProjectQuestionDTO> dtos = projectQuestionMapper.toDtos(list);

        // Populate user details for each question
        dtos.forEach(dto -> {
            // Set askedBy user details
            Optional<FreelancerModel> askedByFreelancer = freelancersRepository.findById(dto.getAskedByUserId());
            if (askedByFreelancer.isPresent()) {
                dto.setAskedByFirstName(askedByFreelancer.get().getFirstName());
                dto.setAskedByLastName(askedByFreelancer.get().getLastName());
                dto.setAskedByAvatar(askedByFreelancer.get().getAvatar());
            }

            // Set answeredBy user details if question is answered
            if (dto.getAnsweredByUserId() != null) {
                // Try to get client details from project or user
                if (project.getClient() != null && project.getClient().getUser() != null &&
                    project.getClient().getUser().getUserId().equals(dto.getAnsweredByUserId())) {
                    dto.setAnsweredByFirstName(project.getClient().getFirstName());
                    dto.setAnsweredByLastName(project.getClient().getLastName());
                    dto.setAnsweredByAvatar(project.getClient().getAvatar());
                }
            }
        });

        return dtos;
    }

    @Override
    @Transactional
    public void deleteQuestion(Long userId, Long projectId, Long questionId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectQuestionModel question = projectQuestionsRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question with id: " + questionId + " not found.")
        );

        if (!question.getProject().getProjectId().equals(project.getProjectId())) {
            throw new ResourceNotFoundException("Question does not belong to project");
        }

        // Only the freelancer who asked the question can delete it
        if (!question.getAskedBy().getUserId().equals(userId)) {
            throw new AccessDeniedException("Only the freelancer who asked the question can delete it.");
        }

        projectQuestionsRepository.delete(question);
    }

    @Override
    @Transactional
    public void deleteAnswer(Long userId, Long projectId, Long questionId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectQuestionModel question = projectQuestionsRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Question with id: " + questionId + " not found.")
        );

        if (!question.getProject().getProjectId().equals(project.getProjectId())) {
            throw new ResourceNotFoundException("Question does not belong to project");
        }

        // Only the project owner (client) can delete their answer
        UserModel clientUser = project.getClient() != null && project.getClient().getUser() != null
                ? project.getClient().getUser()
                : null;

        if (clientUser == null || !clientUser.getUserId().equals(userId)) {
            throw new AccessDeniedException("Only the project owner can delete their answer.");
        }

        if (question.getStatus() != QuestionStatus.ANSWERED) {
            throw new IllegalStateException("Question does not have an answer to delete.");
        }

        // Clear the answer
        question.setAnswerText(null);
        question.setAnsweredBy(null);
        question.setAnsweredAt(null);
        question.setStatus(QuestionStatus.OPEN);

        projectQuestionsRepository.save(question);
    }
}

