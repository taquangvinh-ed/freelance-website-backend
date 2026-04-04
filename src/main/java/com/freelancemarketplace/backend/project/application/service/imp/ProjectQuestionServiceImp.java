package com.freelancemarketplace.backend.project.application.service.imp;

import com.freelancemarketplace.backend.admin.dto.AnswerQuestionRequest;
import com.freelancemarketplace.backend.client.exception.ClientNotFoundException;
import com.freelancemarketplace.backend.freelancer.exception.FreelancerNotFoundException;
import com.freelancemarketplace.backend.notification.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.admin.dto.CreateQuestionRequest;
import com.freelancemarketplace.backend.project.dto.ProjectQuestionDTO;
import com.freelancemarketplace.backend.admin.domain.enums.QuestionStatus;
import com.freelancemarketplace.backend.exceptionHandling.ApiException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.project.exception.ProjectNotFoundException;
import com.freelancemarketplace.backend.project.exception.ProjectQuestionNotFoundException;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.notification.infrastructure.mapper.ClarificationProjectNotificationMapper;
import com.freelancemarketplace.backend.project.infrastructure.mapper.ProjectQuestionMapper;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectClarificationNotificationRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectQuestionsRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.project.application.service.ProjectQuestionService;
import com.freelancemarketplace.backend.user.infrastructure.repository.UserRepository;
import com.freelancemarketplace.backend.user.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.notification.domain.model.ClarificationProjectQANotificationModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectQuestionModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;

@Service
@AllArgsConstructor
public class ProjectQuestionServiceImp implements ProjectQuestionService {

    private final ProjectQuestionsRepository projectQuestionsRepository;
    private final ProjectsRepository projectsRepository;
    private final UserRepository userRepository;
    private final ProjectQuestionMapper projectQuestionMapper;
    private final FreelancersRepository freelancersRepository;
    private final ProjectClarificationNotificationRepository projectClarificationNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ClarificationProjectNotificationMapper clarificationProjectNotificationMapper;
    @Override
    @Transactional
    public ProjectQuestionDTO createQuestion(Long userId, Long projectId, CreateQuestionRequest request) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found.")
        );

        UserModel user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id: " + userId + " not found.")
        );

        Optional<FreelancerModel> freelancerModel = freelancersRepository.findById(userId);

        // Optional: restrict to freelancer role only
        // if (user.getRole() != com.freelancemarketplace.backend.domain.enums.UserRoles.FREELANCER) {
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
        ClarificationProjectQANotificationModel notification = new ClarificationProjectQANotificationModel();
        notification.setQuestionId(saved.getQuestionId());
        notification.setProjectId(project.getProjectId());
        notification.setRecipientUserId(project.getClient() != null && project.getClient().getUser() != null ? project.getClient().getUser().getUserId() : null);
        notification.setProjectTitle(project.getTitle());
        notification.setQuestionText(saved.getQuestionText());
        notification.setSenderFirstName(freelancerModel.isPresent() ? freelancerModel.get().getFirstName() : "Unknown");
        notification.setSenderLastName(freelancerModel.isPresent() ? freelancerModel.get().getLastName() : "User");
        notification.setSenderAvatar(freelancerModel.isPresent() ? freelancerModel.get().getAvatar() : null);
        notification.setType(ClarificationProjectQANotificationModel.NotificationType.NEW_QUESTION);
        notification.setRead(Boolean.FALSE);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        ClarificationProjectQANotificationModel savedNotification = projectClarificationNotificationRepository.save(notification);
        ClarificationiProjectQANotificationDTO notificationDTO = clarificationProjectNotificationMapper.toDto(savedNotification);
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + project.getClient().getClientId(),
                notificationDTO
        );
        return dto;
    }

    @Override
    @Transactional
    public ProjectQuestionDTO answerQuestion(Long userId, Long projectId, Long questionId, AnswerQuestionRequest request) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectQuestionModel question = projectQuestionsRepository.findById(questionId).orElseThrow(
                () -> new ProjectQuestionNotFoundException("Question with id: " + questionId + " not found.")
        );

        if (!question.getProject().getProjectId().equals(project.getProjectId())) {
            throw new ProjectQuestionNotFoundException("Question does not belong to project");
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
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_OPERATION,
                    "Question is not open for answer.");
        }

        UserModel answerer = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id: " + userId + " not found.")
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
        ClarificationProjectQANotificationModel notification = new ClarificationProjectQANotificationModel();
        notification.setQuestionId(saved.getQuestionId());
        notification.setProjectId(project.getProjectId());
        notification.setRecipientUserId(question.getAskedBy().getUserId());
        notification.setProjectTitle(project.getTitle());
        notification.setQuestionText(question.getQuestionText());
        notification.setAnswerText(question.getAnswerText());
        notification.setSenderFirstName(client.getFirstName());
        notification.setSenderLastName(client.getLastName());
        notification.setSenderAvatar(client.getAvatar());
        notification.setType(ClarificationProjectQANotificationModel.NotificationType.NEW_ANSWER);
        notification.setRead(Boolean.FALSE);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        ClarificationProjectQANotificationModel savedNotification = projectClarificationNotificationRepository.save(notification);
        ClarificationiProjectQANotificationDTO notificationDTO = clarificationProjectNotificationMapper.toDto(savedNotification);
        messagingTemplate.convertAndSend(
                "/topic/notifications/" + question.getAskedBy().getUserId(),
                notificationDTO
        );
        return dto;
    }

    @Override
    public List<ProjectQuestionDTO> getQuestionsByProject(Long projectId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found.")
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
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectQuestionModel question = projectQuestionsRepository.findById(questionId).orElseThrow(
                () -> new ProjectQuestionNotFoundException("Question with id: " + questionId + " not found.")
        );

        if (!question.getProject().getProjectId().equals(project.getProjectId())) {
            throw new ProjectQuestionNotFoundException("Question does not belong to project");
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
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectQuestionModel question = projectQuestionsRepository.findById(questionId).orElseThrow(
                () -> new ProjectQuestionNotFoundException("Question with id: " + questionId + " not found.")
        );

        if (!question.getProject().getProjectId().equals(project.getProjectId())) {
            throw new ProjectQuestionNotFoundException("Question does not belong to project");
        }

        // Only the project owner (client) can delete their answer
        UserModel clientUser = project.getClient() != null && project.getClient().getUser() != null
                ? project.getClient().getUser()
                : null;

        if (clientUser == null || !clientUser.getUserId().equals(userId)) {
            throw new AccessDeniedException("Only the project owner can delete their answer.");
        }

        if (question.getStatus() != QuestionStatus.ANSWERED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_OPERATION,
                    "Question does not have an answer to delete.");
        }

        // Clear the answer
        question.setAnswerText(null);
        question.setAnsweredBy(null);
        question.setAnsweredAt(null);
        question.setStatus(QuestionStatus.OPEN);

        projectQuestionsRepository.save(question);
    }
}

