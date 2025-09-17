package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.Q_ADTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.exception.Q_AException;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.Q_AService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/admin/qa", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QAController {

    private final Q_AService qAService;

    public QAController(Q_AService qAService) {
        this.qAService = qAService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDTO> createQA(@RequestBody @Valid Q_ADTO qAdto){
        try{
            Q_ADTO created = qAService.createQA(qAdto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDTO(
                            ResponseStatusCode.CREATED,
                            ResponseMessage.CREATED,
                            created
                    ));
        }catch (RuntimeException e){
            throw new Q_AException("Message: "+e);
        }
    }

    @PutMapping("/{qaId}")
    ResponseEntity<ResponseDTO>updateQA(@PathVariable Long qaId,
                                        @RequestBody Q_ADTO qAdto){
        Q_ADTO updatedQA = qAService.updateQA(qaId, qAdto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        updatedQA
                ));
    }
}
