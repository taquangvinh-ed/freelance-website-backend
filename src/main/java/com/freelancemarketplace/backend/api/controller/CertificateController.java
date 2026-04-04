package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.dto.CertificateDTO;
import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/certificate", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping
    public ApiResponse<?> createCertificate(@AuthenticationPrincipal AppUser appUser, @RequestBody CertificateDTO certificateDTO){
        Long freelancerId = appUser.getId();
        CertificateDTO newCertificate = certificateService.createCertificate(freelancerId, certificateDTO);
        return ApiResponse.created(newCertificate);
    }


    @PutMapping("/{certificateId}")
    public ApiResponse<?> updateCertificate(@PathVariable Long certificateId, @RequestBody CertificateDTO certificateDTO){
        CertificateDTO updateCertificate = certificateService.updateCertificate(certificateId, certificateDTO);
        return ApiResponse.success(updateCertificate);
    }

    @DeleteMapping("/{certificateId}")
    public ApiResponse<?> delete(@PathVariable Long certificateId){
         certificateService.deleteCertificate(certificateId);
        return ApiResponse.delete();
    }

    @GetMapping("/getAll")
     public ApiResponse<?> getAll(@AuthenticationPrincipal AppUser appUser){
        Long freelancerId = appUser.getId();
        List<CertificateDTO> list = certificateService.getAllCertificateByFreelancer(freelancerId);
        return ApiResponse.success(list);
    }
}
