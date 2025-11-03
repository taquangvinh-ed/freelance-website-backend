package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ClockifyStartRequest;
import com.freelancemarketplace.backend.dto.ClockifyTimeEntryResponse;
import com.freelancemarketplace.backend.model.TimeLog;
import com.freelancemarketplace.backend.repository.TimeLogRepository;
import com.freelancemarketplace.backend.service.ClockifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/tracker", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ClockifyController {

    private final ClockifyService clockifyService;
    private final TimeLogRepository timeLogRepository;

    @PostMapping("/start/{contractId}")
    public ResponseEntity<ClockifyTimeEntryResponse> startTimer(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable Long contractId, // Vẫn cần Contract ID cho các logic khác
            @RequestBody ClockifyStartRequest request) {

        // 1. Logic Lấy API Key cá nhân từ DB
        Long freelancerId = appUser.getId();

        // 3. Cập nhật Request Body và Gọi Service Clockify
        request.setStart(java.time.Instant.now().toString());

        // Gọi Service mà không cần truyền workspaceId
        ClockifyTimeEntryResponse response = clockifyService.startTimeEntry(
                freelancerId,
                contractId,
                request
        );


        return ResponseEntity.ok(response);
    }

    @PostMapping("/stop")
    public ResponseEntity<ClockifyTimeEntryResponse> stopTimer(
            @AuthenticationPrincipal AppUser appUser) {

        Long freelancerId = appUser.getId();

        ClockifyTimeEntryResponse response = clockifyService.stopTimeEntry(freelancerId);

        return ResponseEntity.ok(response);
    }

    // --- 3. CHECK ACTIVE TIMER ---

    /**
     * Kiểm tra xem Freelancer có timer nào đang chạy không (dùng khi Frontend tải trang).
     * Trả về TimeLog Model từ DB của bạn.
     */
//    @GetMapping("/active")
//    public ResponseEntity<TimeLog> checkActiveTimer(
//            @AuthenticationPrincipal AppUser appUser) {
//
//        Long freelancerId = appUser.getId();
//
//        // Tìm bản ghi đang ACTIVE trong DB của bạn
//        Optional<TimeLog> activeLog = timeLogRepository.findActiveTimeLogByFreelancerId(freelancerId);
//
//        return activeLog
//                .map(ResponseEntity::ok) // Nếu có, trả về 200 OK
//                .orElseGet(() -> ResponseEntity.notFound().build()); // Nếu không, trả về 404 Not Found
//    }

}
