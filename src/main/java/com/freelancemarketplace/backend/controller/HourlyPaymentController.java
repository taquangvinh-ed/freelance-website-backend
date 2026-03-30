package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.HourlyPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class HourlyPaymentController {

    private final HourlyPaymentService hourlyPaymentService;

    @GetMapping("/contracts/{contractId}/hourly-payments")
    public ResponseEntity<ResponseDTO<List<HourlyPaymentSummaryDTO>>> getHourlyPaymentsByContract(
            @PathVariable Long contractId,
            @AuthenticationPrincipal AppUser appUser
    ) {
        List<HourlyPaymentSummaryDTO> body = hourlyPaymentService.getHourlyPaymentsByContract(contractId, appUser.getId());
        return ResponseEntity.ok(new ResponseDTO<>(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, body));
    }

    @GetMapping("/hourly-payments/{paymentId}")
    public ResponseEntity<ResponseDTO<HourlyPaymentDetailDTO>> getHourlyPaymentDetail(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal AppUser appUser
    ) {
        HourlyPaymentDetailDTO body = hourlyPaymentService.getHourlyPaymentById(paymentId, appUser.getId());
        return ResponseEntity.ok(new ResponseDTO<>(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, body));
    }

    @PatchMapping("/hourly-payments/{paymentId}/approve")
    public ResponseEntity<ResponseDTO<HourlyPaymentDetailDTO>> approveHourlyPayment(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody(required = false) HourlyPaymentActionRequestDTO request
    ) {
        String note = request != null ? request.getNote() : null;
        HourlyPaymentDetailDTO body = hourlyPaymentService.approveHourlyPayment(paymentId, appUser.getId(), note);
        return ResponseEntity.ok(new ResponseDTO<>(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, body));
    }

    @PatchMapping("/hourly-payments/{paymentId}/dispute")
    public ResponseEntity<ResponseDTO<HourlyPaymentDetailDTO>> disputeHourlyPayment(
            @PathVariable Long paymentId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody(required = false) HourlyPaymentActionRequestDTO request
    ) {
        String note = request != null ? request.getNote() : null;
        HourlyPaymentDetailDTO body = hourlyPaymentService.disputeHourlyPayment(paymentId, appUser.getId(), note);
        return ResponseEntity.ok(new ResponseDTO<>(ResponseStatusCode.SUCCESS, ResponseMessage.SUCCESS, body));
    }
}

