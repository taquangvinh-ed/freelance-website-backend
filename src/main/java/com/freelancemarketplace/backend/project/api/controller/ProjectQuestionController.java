package com.freelancemarketplace.backend.project.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.admin.dto.AnswerQuestionRequest;
import com.freelancemarketplace.backend.admin.dto.CreateQuestionRequest;
import com.freelancemarketplace.backend.project.dto.ProjectQuestionDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.project.application.service.ProjectQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/projects/{projectId}/questions", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ProjectQuestionController {

    private final ProjectQuestionService projectQuestionService;

    @PostMapping
    public ApiResponse<?> createQuestion(@PathVariable Long projectId,
                                                      @AuthenticationPrincipal AppUser appUser,
                                                      @Valid @RequestBody CreateQuestionRequest request) {
        Long userId = appUser.getId();
        ProjectQuestionDTO dto = projectQuestionService.createQuestion(userId, projectId, request);
        return ApiResponse.created(dto);
    }

    @GetMapping
    public ApiResponse<?> getQuestions(@PathVariable Long projectId) {
        List<ProjectQuestionDTO> list = projectQuestionService.getQuestionsByProject(projectId);
        return ApiResponse.success(list);
    }

    @PutMapping("/{questionId}/answer")
    public ApiResponse<?> answerQuestion(@PathVariable Long projectId,
                                                      @PathVariable Long questionId,
                                                      @AuthenticationPrincipal AppUser appUser,
                                                      @Valid @RequestBody AnswerQuestionRequest request) {
        Long userId = appUser.getId();
        ProjectQuestionDTO dto = projectQuestionService.answerQuestion(userId, projectId, questionId, request);
        return ApiResponse.success(dto);
    }

    @DeleteMapping("/{questionId}")
    public ApiResponse<?> deleteQuestion(@PathVariable Long projectId,
                                                        @PathVariable Long questionId,
                                                        @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        projectQuestionService.deleteQuestion(userId, projectId, questionId);
        return ApiResponse.success("Question deleted successfully");
    }

    @DeleteMapping("/{questionId}/answer")
    public ApiResponse<?> deleteAnswer(@PathVariable Long projectId,
                                                      @PathVariable Long questionId,
                                                      @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        projectQuestionService.deleteAnswer(userId, projectId, questionId);
        return ApiResponse.success("Answer deleted successfully");
    }
}
