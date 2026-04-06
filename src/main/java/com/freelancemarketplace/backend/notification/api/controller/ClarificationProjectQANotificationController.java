package com.freelancemarketplace.backend.notification.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.notification.dto.ClarificationiProjectQANotificationDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.notification.application.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/notifications/clarification-qa", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ClarificationProjectQANotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public ApiResponse<?> getMyNotifications(@AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        List<ClarificationiProjectQANotificationDTO> notifications =
                notificationService.getClarificationProjectQANotificationsByUserId(userId);
        return ApiResponse.success(notifications);
    }

    @PatchMapping("/{notificationId}/read")
    public ApiResponse<?> markAsRead(@PathVariable Long notificationId) {
        notificationService.markClarificationPrjectQANotificationAsRead(notificationId);
        return ApiResponse.success("Notification marked as read");
    }
}

