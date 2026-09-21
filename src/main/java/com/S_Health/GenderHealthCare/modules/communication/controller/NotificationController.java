package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.communication.dto.request.NotificationRequest;
import com.S_Health.GenderHealthCare.modules.communication.dto.response.notification.NotificationResponse;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.modules.communication.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@SecurityRequirement(name = "api")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @Operation(summary = CommunicationMessages.CREATE_NOTIFICATION)
    public ApiResponse<NotificationResponse> create(@Valid @RequestBody NotificationRequest request) {
        return ApiResponse.success(notificationService.createNotification(request), null);
    }

    @GetMapping
    @Operation(summary = CommunicationMessages.GET_NOTIFICATIONS)
    public List<NotificationResponse> getAll() {
        return notificationService.getNotificationsByUser();
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = CommunicationMessages.GET_NOTIFICATION)
    public NotificationResponse getById(@PathVariable Long notificationId) {
        return notificationService.getNotificationById(notificationId);
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = CommunicationMessages.MARK_NOTIFICATION_READ)
    public ApiResponse<String> markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ApiResponse.success(CommunicationMessages.NOTIFICATIONS_MARKED_READ, null);
    }

    @PatchMapping("/read-all")
    @Operation(summary = CommunicationMessages.MARK_ALL_NOTIFICATIONS_READ)
    public ApiResponse<String> markAllRead() {
        notificationService.markAllAsReadForCurrentUser();
        return ApiResponse.success(CommunicationMessages.NOTIFICATIONS_MARKED_READ, null);
    }

    @GetMapping("/unread-count")
    @Operation(summary = CommunicationMessages.GET_UNREAD_NOTIFICATION_COUNT)
    public Long unreadCount() {
        return notificationService.countUnreadForCurrentUser();
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = CommunicationMessages.DELETE_NOTIFICATION)
    public ApiResponse<String> delete(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ApiResponse.success(CommunicationMessages.NOTIFICATION_DELETED, null);
    }
}
