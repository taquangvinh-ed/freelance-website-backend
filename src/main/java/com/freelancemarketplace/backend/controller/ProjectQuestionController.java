package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.AnswerQuestionRequest;
import com.freelancemarketplace.backend.dto.CreateQuestionRequest;
import com.freelancemarketplace.backend.dto.ProjectQuestionDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ProjectQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/projects/{projectId}/questions", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ProjectQuestionController {

    private final ProjectQuestionService projectQuestionService;

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> createQuestion(@PathVariable Long projectId,
                                                      @AuthenticationPrincipal AppUser appUser,
                                                      @Valid @RequestBody CreateQuestionRequest request) {
        Long userId = appUser.getId();
        ProjectQuestionDTO dto = projectQuestionService.createQuestion(userId, projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(
                ResponseStatusCode.CREATED,
                ResponseMessage.CREATED,
                dto
        ));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> getQuestions(@PathVariable Long projectId) {
        List<ProjectQuestionDTO> list = projectQuestionService.getQuestionsByProject(projectId);
        return ResponseEntity.ok(new ResponseDTO<>(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                list
        ));
    }

    @PutMapping("/{questionId}/answer")
    public ResponseEntity<ResponseDTO<?>> answerQuestion(@PathVariable Long projectId,
                                                      @PathVariable Long questionId,
                                                      @AuthenticationPrincipal AppUser appUser,
                                                      @Valid @RequestBody AnswerQuestionRequest request) {
        Long userId = appUser.getId();
        ProjectQuestionDTO dto = projectQuestionService.answerQuestion(userId, projectId, questionId, request);
        return ResponseEntity.ok(new ResponseDTO<>(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                dto
        ));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<ResponseDTO<?>> deleteQuestion(@PathVariable Long projectId,
                                                        @PathVariable Long questionId,
                                                        @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        projectQuestionService.deleteQuestion(userId, projectId, questionId);
        return ResponseEntity.ok(new ResponseDTO<>(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                "Question deleted successfully"
        ));
    }

    @DeleteMapping("/{questionId}/answer")
    public ResponseEntity<ResponseDTO<?>> deleteAnswer(@PathVariable Long projectId,
                                                      @PathVariable Long questionId,
                                                      @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        projectQuestionService.deleteAnswer(userId, projectId, questionId);
        return ResponseEntity.ok(new ResponseDTO<>(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                "Answer deleted successfully"
        ));
    }
}
