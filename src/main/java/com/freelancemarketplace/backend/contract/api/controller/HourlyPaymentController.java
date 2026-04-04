package com.freelancemarketplace.backend.contract.api.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.contract.application.service.HourlyPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.freelancemarketplace.backend.contract.dto.HourlyPaymentActionRequestDTO;
import com.freelancemarketplace.backend.contract.dto.HourlyPaymentDetailDTO;
import com.freelancemarketplace.backend.contract.dto.HourlyPaymentSummaryDTO;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class HourlyPaymentController {

    private final HourlyPaymentService hourlyPaymentService;

    @GetMapping("/contracts/{contractId}/hourly-payments")
    public ApiResponse<?> getHourlyPaymentsByContract(
            @PathVariable Long contractId,
            @AuthenticationPrincipal AppUser appUser
    ) {
        List<HourlyPaymentSummaryDTO> body = hourlyPaymentService.getHourlyPaymentsByContract(contractId, appUser.getId());
        return ApiResponse.success(body);
    }

    @GetMapping("/hourly-payments/{paymentId}")
    public ApiResponse<?> getHourlyPaymentDetail(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal AppUser appUser
    ) {
        HourlyPaymentDetailDTO body = hourlyPaymentService.getHourlyPaymentById(paymentId, appUser.getId());
        return ApiResponse.success(body);
    }

    @PatchMapping("/hourly-payments/{paymentId}/approve")
    public ApiResponse<?> approveHourlyPayment(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody(required = false) HourlyPaymentActionRequestDTO request
    ) {
        String note = request != null ? request.getNote() : null;
        HourlyPaymentDetailDTO body = hourlyPaymentService.approveHourlyPayment(paymentId, appUser.getId(), note);
        return ApiResponse.success(body);
    }

    @PatchMapping("/hourly-payments/{paymentId}/dispute")
    public ApiResponse<?> disputeHourlyPayment(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody(required = false) HourlyPaymentActionRequestDTO request
    ) {
        String note = request != null ? request.getNote() : null;
        HourlyPaymentDetailDTO body = hourlyPaymentService.disputeHourlyPayment(paymentId, appUser.getId(), note);
        return ApiResponse.success(body);
    }
}

