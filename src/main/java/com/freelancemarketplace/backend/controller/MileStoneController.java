package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.MileStoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/milestone", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MileStoneController {

    private MileStoneService mileStoneService;

    public MileStoneController(MileStoneService mileStoneService) {
        this.mileStoneService = mileStoneService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO>createMileStone(@RequestBody MileStoneDTO mileStoneDTO){
        MileStoneDTO newMileStone = mileStoneService.createMileStone(mileStoneDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newMileStone
                ));
    }

    @PutMapping("/{mileStoneId}")
    public ResponseEntity<ResponseDTO>updateMileStone(@PathVariable Long mileStoneId,
            @RequestBody MileStoneDTO mileStoneDTO){
        MileStoneDTO updatedMileStone = mileStoneService.update(mileStoneId, mileStoneDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedMileStone
                ));


    }


    @DeleteMapping("/{mileStoneId}")
    public ResponseEntity<ResponseDTO>deleteMileStone(@PathVariable Long mileStoneId){

        mileStoneService.deleteMileStone(mileStoneId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<ResponseDTO>getAllMiStoneByContractId(@PathVariable Long contractId){

        List<MileStoneDTO> mileStones = mileStoneService.getAllMileStoneByContract(contractId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        mileStones
                ));
    }


}
