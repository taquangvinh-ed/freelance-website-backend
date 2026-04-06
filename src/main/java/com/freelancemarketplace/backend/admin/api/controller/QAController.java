package com.freelancemarketplace.backend.admin.api.controller;

import com.freelancemarketplace.backend.admin.dto.Q_ADTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.admin.application.service.Q_AService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/admin/qa", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QAController {

    private final Q_AService qAService;

    public QAController(Q_AService qAService) {
        this.qAService = qAService;
    }

    @PostMapping("/")
    public ApiResponse<?> createQA(@RequestBody @Valid Q_ADTO qAdto) {
        Q_ADTO created = qAService.createQA(qAdto);
        return ApiResponse.created(created);

    }

    @PutMapping("/{qaId}")
    ApiResponse<?> updateQA(@PathVariable Long qaId,
                                         @RequestBody Q_ADTO qAdto) {
        Q_ADTO updatedQA = qAService.updateQA(qaId, qAdto);
        return ApiResponse.success(updatedQA);
    }

    @DeleteMapping("/{qaId}")
    ApiResponse<?> deleteQA(@PathVariable Long qaId) {
        qAService.deleteQA(qaId);
        return ApiResponse.noContent();
    }

    @GetMapping("/getAll")
    public ApiResponse<?> getAllQA() {
        List<Q_ADTO> AllQAs = qAService.getAll();
        return ApiResponse.success(AllQAs);
    }

    @GetMapping("/getAll/byTag")
    public ApiResponse<?> getAllQAByTag(@RequestParam("tag") String tag) {
        List<Q_ADTO> AllQAsByTag = qAService.getAllByTag(tag);
        return ApiResponse.success(AllQAsByTag);
    }

    @GetMapping("/getAll/byAdmin")
    public ApiResponse<?> getAllQAByAdmin(@RequestParam("adminId") Long adminId) {
        List<Q_ADTO> AllQAsByTag = qAService.getAllByAdminId(adminId);
        return ApiResponse.success(AllQAsByTag);
    }


}
