package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.CertificateDTO;
import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/certificate", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    ResponseEntity<CertificateDTO> createCertificate(@AuthenticationPrincipal AppUser appUser, @RequestBody CertificateDTO certificateDTO){
        Long freelancerId = appUser.getId();
        CertificateDTO newCertificate = certificateService.createCertificate(freelancerId, certificateDTO);
        return ResponseEntity.ok(newCertificate);
    }


    @PutMapping("/{certificateId}")
    ResponseEntity<CertificateDTO> updateCertificate(@PathVariable Long certificateId, @RequestBody CertificateDTO certificateDTO){
        CertificateDTO updateCertificate = certificateService.updateCertificate(certificateId, certificateDTO);
        return ResponseEntity.ok(updateCertificate);
    }

    @DeleteMapping("/{certificateId}")
    ResponseEntity<Void> delete(@PathVariable Long certificateId){
         certificateService.deleteCertificate(certificateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    ResponseEntity<List<CertificateDTO>> getAll(@AuthenticationPrincipal AppUser appUser){
        Long freelancerId = appUser.getId();
        List<CertificateDTO> list = certificateService.getAllCertificateByFreelancer(freelancerId);
        return ResponseEntity.ok(list);
    }
}
