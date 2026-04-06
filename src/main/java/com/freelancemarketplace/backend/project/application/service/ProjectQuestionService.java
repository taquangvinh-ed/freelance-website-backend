package com.freelancemarketplace.backend.project.application.service;

import com.freelancemarketplace.backend.admin.dto.AnswerQuestionRequest;
import com.freelancemarketplace.backend.admin.dto.CreateQuestionRequest;
import com.freelancemarketplace.backend.project.dto.ProjectQuestionDTO;

import java.util.List;

public interface ProjectQuestionService {
    ProjectQuestionDTO createQuestion(Long userId, Long projectId, CreateQuestionRequest request);
    ProjectQuestionDTO answerQuestion(Long userId, Long projectId, Long questionId, AnswerQuestionRequest request);
    List<ProjectQuestionDTO> getQuestionsByProject(Long projectId);
    void deleteQuestion(Long userId, Long projectId, Long questionId);
    void deleteAnswer(Long userId, Long projectId, Long questionId);
}

