package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/api/contracts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }



    @PutMapping("/{contractId}")
    ResponseEntity<ResponseDTO>updateContract(@PathVariable Long contractId,
                                             @RequestBody ContractDTO contractDTO){
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
    ResponseEntity<ResponseDTO>deleteContract(@PathVariable Long contractId){
        contractService.deleteContract(contractId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));

    }

    @GetMapping("/freelancer/{freelancerId}")
    ResponseEntity<ResponseDTO>findAllContractByFreelancer(@PathVariable Long freelancerId){

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
    ResponseEntity<ContractResponseDTO> getContractById(@PathVariable Long contractId){
        ContractResponseDTO contract = contractService.getContractById(contractId);
        return  ResponseEntity.ok(contract);
    }

    @PostMapping("/{contractId}/milestones/{milestoneId}/pay")
    ResponseEntity<MileStoneDTO> payMilestone(@PathVariable Long contractId, @PathVariable Long milestoneId) throws Exception {
        MileStoneDTO updatedMilestone = contractService.processMilestonePayment(contractId, milestoneId);
        return ResponseEntity.ok(updatedMilestone);
    }

    @PostMapping("/{contractId}/milestones/{milestoneId}/complete")
    ResponseEntity<Timestamp> markMilestoneAsCompleted(@PathVariable Long contractId, @PathVariable Long milestoneId) throws Exception {
        log.info("Da nhan duoc yeu cau");
        Timestamp result = contractService.markMilestoneAsCompleted(contractId, milestoneId);
        return ResponseEntity.ok(result);
    }

}
