package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/notifications/clarification-qa", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ClarificationProjectQANotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<ResponseDTO<?>> getMyNotifications(@AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        List<ClarificationiProjectQANotificationDTO> notifications =
                notificationService.getClarificationProjectQANotificationsByUserId(userId);
        return ResponseEntity.ok(new ResponseDTO<>(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                notifications
        ));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ResponseDTO<?>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markClarificationPrjectQANotificationAsRead(notificationId);
        return ResponseEntity.ok(new ResponseDTO<>(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                "Notification marked as read"
        ));
    }
}

