package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ContractDTO;
import com.freelancemarketplace.backend.dto.ContractResponseDTO;
import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/contracts", produces = {MediaType.APPLICATION_JSON_VALUE})
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

}
