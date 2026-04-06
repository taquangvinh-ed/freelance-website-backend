package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.contract.dto.ContractDTO;
import com.freelancemarketplace.backend.contract.dto.ContractResponseDTO;
import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.contract.dto.WeeklyReportDTO;
import com.freelancemarketplace.backend.project.application.service.CloudinaryService;
import com.freelancemarketplace.backend.contract.application.service.ContractReportingService;
import com.freelancemarketplace.backend.contract.application.service.ContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/contracts", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Slf4j
public class ContractController {

    private final ContractService contractService;
    private final ContractReportingService contractReportingService;
    private final CloudinaryService cloudinaryService;

    @PutMapping("/{contractId}")
    public ApiResponse<?> updateContract(@PathVariable Long contractId,
                                               @RequestBody ContractDTO contractDTO) {
        ContractDTO updatedContract = contractService.updateContract(contractId, contractDTO);

        return ApiResponse.success(updatedContract);
    }

    @DeleteMapping("/{contractId}")
    public ApiResponse<?> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return ApiResponse.noContent();
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ApiResponse<?> findAllContractByFreelancer(@PathVariable Long freelancerId) {

        List<ContractDTO> contracts = contractService.findAllContractByFreelancerId(freelancerId);

        return ApiResponse.success(contracts);

    }

    @GetMapping("/get-contract/{contractId}")
    public ApiResponse<?> getContractById(@PathVariable Long contractId) {
        ContractResponseDTO contract = contractService.getContractById(contractId);
        return ApiResponse.success(contract);
    }

    @PostMapping("/{contractId}/milestones/{milestoneId}/pay")
    public ApiResponse<?> payMilestone(@PathVariable Long contractId, @PathVariable Long milestoneId) throws Exception {
        MileStoneDTO updatedMilestone = contractService.processMilestonePayment(contractId, milestoneId);
        return ApiResponse.success(updatedMilestone);
    }

    @PostMapping("/{contractId}/milestones/{milestoneId}/complete")
    public ApiResponse<?> markMilestoneAsCompleted(@PathVariable Long contractId, @PathVariable Long milestoneId,
                                                       @RequestParam (required = false) MultipartFile file) throws Exception {
        log.info("Da nhan duoc yeu cau");
        Timestamp result = contractService.markMilestoneAsCompleted(contractId, milestoneId, file);
        return ApiResponse.success(result);
    }

    @GetMapping("/{contractId}/hourly-contract-logs")
    public ApiResponse<?> getLogs(@PathVariable Long contractId) {
        log.info("ContractId: {}", contractId);
        List<WeeklyReportDTO> logs = contractReportingService.getAllLogs(contractId);
        return ApiResponse.success(logs);
    }

    @PostMapping("/milestone-attachment")
    ResponseEntity<?> abc(@RequestParam MultipartFile file) {
        if (file.isEmpty())
            return new ResponseEntity<>("File can not be empty", HttpStatus.BAD_REQUEST);
        try {
            String fileUrl = cloudinaryService.uploadFileMilestone(file);
            return new ResponseEntity<>(Map.of(
                    "fileUrl", fileUrl,
                    "fileName", file.getOriginalFilename()
            ),
                    HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
