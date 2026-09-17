package com.S_Health.GenderHealthCare.modules.communication.controller;

import com.S_Health.GenderHealthCare.dto.request.notification.NotificationRequest;
import com.S_Health.GenderHealthCare.dto.response.nofitication.NotificationResponse;
import com.S_Health.GenderHealthCare.modules.communication.service.NotificationService;
import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@SecurityRequirement(name = "api")
public class LegacyNotificationController {
    @Autowired
    NotificationService notificationService;

    @PostMapping
    @Operation(summary = CommunicationMessages.CREATE_NOTIFICATION)
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        return ResponseEntity.status(201).body(notificationService.createNotification(request));
    }

    // hoàn thành ở frondend
    @GetMapping
    @Operation(summary = CommunicationMessages.GET_NOTIFICATIONS)
    public ResponseEntity<List<NotificationResponse>> getAll() {
        return ResponseEntity.ok(notificationService.getNotificationsByUser());
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = CommunicationMessages.GET_NOTIFICATION)
    public ResponseEntity<NotificationResponse> getById(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.getNotificationById(notificationId));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = CommunicationMessages.MARK_NOTIFICATION_READ)
    public ResponseEntity<Void> markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    @Operation(summary = CommunicationMessages.MARK_ALL_NOTIFICATIONS_READ)
    public ResponseEntity<Void> markAllRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    @Operation(summary = CommunicationMessages.GET_UNREAD_NOTIFICATION_COUNT)
    public ResponseEntity<Long> countUnread(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.countUnread(userId));
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = CommunicationMessages.DELETE_NOTIFICATION)
    public ResponseEntity<Void> delete(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}
