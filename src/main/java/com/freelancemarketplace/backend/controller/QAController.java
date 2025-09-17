package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.Q_ADTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.Q_AService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO> createQA(@RequestBody @Valid Q_ADTO qAdto) {
        Q_ADTO created = qAService.createQA(qAdto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        created
                ));

    }

    @PutMapping("/{qaId}")
    ResponseEntity<ResponseDTO> updateQA(@PathVariable Long qaId,
                                         @RequestBody Q_ADTO qAdto) {
        Q_ADTO updatedQA = qAService.updateQA(qaId, qAdto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedQA
                ));
    }

    @DeleteMapping("/{qaId}")
    ResponseEntity<ResponseDTO> deleteQA(@PathVariable Long qaId) {
        qAService.deleteQA(qaId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllQA() {
        List<Q_ADTO> AllQAs = qAService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        AllQAs
                ));
    }

    @GetMapping("/getAll/byTag")
    public ResponseEntity<ResponseDTO> getAllQAByTag(@RequestParam("tag") String tag) {
        List<Q_ADTO> AllQAsByTag = qAService.getAllByTag(tag);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        AllQAsByTag
                ));
    }

    @GetMapping("/getAll/byAdmin")
    public ResponseEntity<ResponseDTO> getAllQAByAdmin(@RequestParam("adminId") Long adminId) {
        List<Q_ADTO> AllQAsByTag = qAService.getAllByAdminId(adminId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        AllQAsByTag
                ));
    }


}
