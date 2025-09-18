package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ContractDTO;
import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/contracts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO>createContract(@RequestBody ContractDTO contractDTO){
        ContractDTO newContract = contractService.createContract(contractDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newContract
                ));
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


}
