package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.CloudinaryService;
import com.freelancemarketplace.backend.service.ContractReportingService;
import com.freelancemarketplace.backend.service.ContractService;
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
    ResponseEntity<ResponseDTO> updateContract(@PathVariable Long contractId,
                                               @RequestBody ContractDTO contractDTO) {
        ContractDTO updatedContract = contractService.updateContract(contractId, contractDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedContract
                ));


    }

    @DeleteMapping("/{contractId}")
    ResponseEntity<ResponseDTO> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));

    }

    @GetMapping("/freelancer/{freelancerId}")
    ResponseEntity<ResponseDTO> findAllContractByFreelancer(@PathVariable Long freelancerId) {

        List<ContractDTO> contracts = contractService.findAllContractByFreelancerId(freelancerId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        contracts
                ));

    }

    @GetMapping("/get-contract/{contractId}")
    ResponseEntity<ContractResponseDTO> getContractById(@PathVariable Long contractId) {
        ContractResponseDTO contract = contractService.getContractById(contractId);
        return ResponseEntity.ok(contract);
    }

    @PostMapping("/{contractId}/milestones/{milestoneId}/pay")
    ResponseEntity<MileStoneDTO> payMilestone(@PathVariable Long contractId, @PathVariable Long milestoneId) throws Exception {
        MileStoneDTO updatedMilestone = contractService.processMilestonePayment(contractId, milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }

    @PostMapping("/{contractId}/milestones/{milestoneId}/complete")
    ResponseEntity<Timestamp> markMilestoneAsCompleted(@PathVariable Long contractId, @PathVariable Long milestoneId,
                                                       @RequestParam (required = false) MultipartFile file) throws Exception {
        log.info("Da nhan duoc yeu cau");
        Timestamp result = contractService.markMilestoneAsCompleted(contractId, milestoneId, file);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{contractId}/hourly-contract-logs")
    public ResponseEntity<List<WeeklyReportDTO>> getLogs(@PathVariable Long contractId) {
        log.info("ContractId: {}", contractId);
        List<WeeklyReportDTO> logs = contractReportingService.getAllLogs(contractId);
        return ResponseEntity.ok(logs);
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
