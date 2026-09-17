package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.dto.request.notification.NotificationRequest;
import com.S_Health.GenderHealthCare.dto.response.nofitication.NotificationResponse;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.modules.communication.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createNotification(request));
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
    public void markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }

    @PatchMapping("/read-all")
    @Operation(summary = CommunicationMessages.MARK_ALL_NOTIFICATIONS_READ)
    public void markAllRead() {
        notificationService.markAllAsReadForCurrentUser();
    }

    @GetMapping("/unread-count")
    @Operation(summary = CommunicationMessages.GET_UNREAD_NOTIFICATION_COUNT)
    public Long unreadCount() {
        return notificationService.countUnreadForCurrentUser();
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = CommunicationMessages.DELETE_NOTIFICATION)
    public void delete(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }
}
