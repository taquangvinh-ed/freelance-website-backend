package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AnswerQuestionRequest;
import com.freelancemarketplace.backend.dto.CreateQuestionRequest;
import com.freelancemarketplace.backend.dto.ProjectQuestionDTO;

import java.util.List;

public interface ProjectQuestionService {
    ProjectQuestionDTO createQuestion(Long userId, Long projectId, CreateQuestionRequest request);
    ProjectQuestionDTO answerQuestion(Long userId, Long projectId, Long questionId, AnswerQuestionRequest request);
    List<ProjectQuestionDTO> getQuestionsByProject(Long projectId);
    void deleteQuestion(Long userId, Long projectId, Long questionId);
    void deleteAnswer(Long userId, Long projectId, Long questionId);
}

