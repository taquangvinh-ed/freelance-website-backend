package com.freelancemarketplace.backend.contract.api.controller;

import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.contract.application.service.MileStoneService;
import org.springframework.http.MediaType;
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
    public ApiResponse<?> createMileStone(@RequestBody MileStoneDTO mileStoneDTO){
        MileStoneDTO newMileStone = mileStoneService.createMileStone(mileStoneDTO);
        return ApiResponse.created(newMileStone);
    }

    @PutMapping("/{mileStoneId}")
    public ApiResponse<?> updateMileStone(@PathVariable Long mileStoneId,
            @RequestBody MileStoneDTO mileStoneDTO){
        MileStoneDTO updatedMileStone = mileStoneService.update(mileStoneId, mileStoneDTO);
        return ApiResponse.success(updatedMileStone);


    }


    @DeleteMapping("/{mileStoneId}")
    public ApiResponse<?> deleteMileStone(@PathVariable Long mileStoneId){

        mileStoneService.deleteMileStone(mileStoneId);
        return ApiResponse.noContent();
    }

    @GetMapping("/contract/{contractId}")
    public ApiResponse<?> getAllMiStoneByContractId(@PathVariable Long contractId){

        List<MileStoneDTO> mileStones = mileStoneService.getAllMileStoneByContract(contractId);
        return ApiResponse.success(mileStones);
    }


}
