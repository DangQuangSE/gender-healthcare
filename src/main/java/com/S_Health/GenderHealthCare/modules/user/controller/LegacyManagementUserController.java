package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.request.CreateUserRequest;
import com.S_Health.GenderHealthCare.modules.user.dto.request.UpdateConsultantSpecializationRequest;
import com.S_Health.GenderHealthCare.modules.user.service.ManageUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
@SecurityRequirement(name = "api")

/**
 * Legacy compatibility controller. Use modules.user.controller.UserManagementController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyManagementUserController {
    private final ManageUserService manageUserService;

    public LegacyManagementUserController(ManageUserService manageUserService) {
        this.manageUserService = manageUserService;
    }
    
    @PostMapping("/user")
    @Operation(summary = UserMessages.CREATE_STAFF_ACCOUNT)
    public ResponseEntity createAccount(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(manageUserService.createStaffAccount(request));
    }

    @PostMapping("/user/{userId}/specializations")
    @Operation(summary = UserMessages.ADD_SPECIALIZATIONS)
    public ResponseEntity addSpecializationsToConsultant(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateConsultantSpecializationRequest request) {
        return ResponseEntity.ok(manageUserService.addSpecializationsToConsultant(userId, request.getSpecializationIds()));
    }
    
    @DeleteMapping("/user/{userId}/specializations/{specializationId}")
    @Operation(summary = UserMessages.REMOVE_SPECIALIZATION)
    public ResponseEntity removeSpecializationFromConsultant(
            @PathVariable Long userId,
            @PathVariable Long specializationId) {
        manageUserService.removeSpecializationFromConsultant(userId, specializationId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user/{userId}/specializations")
    @Operation(summary = UserMessages.GET_SPECIALIZATIONS)
    public ResponseEntity getConsultantSpecializations(@PathVariable Long userId) {
        return ResponseEntity.ok(manageUserService.getConsultantSpecializations(userId));
    }
    @GetMapping("/users")
    @Operation(summary = UserMessages.GET_USERS_BY_ROLE)
    public ResponseEntity getUsersByRole(@RequestParam String role) {
        return ResponseEntity.ok(manageUserService.getUsersByRole(role));
    }

    @DeleteMapping("/user/{userId}")
    @Operation(summary = UserMessages.DEACTIVATE_USER)
    public ResponseEntity<?> softDeleteUser(@PathVariable Long userId) {
        manageUserService.softDeleteUser(userId);
        return ResponseEntity.ok().body(Map.of(
            "message", UserMessages.USER_DEACTIVATED_SUCCESS,
            "userId", userId,
            "timestamp", java.time.LocalDateTime.now()
        ));
    }

    @PutMapping("/user/{userId}/restore")
    @Operation(summary = UserMessages.RESTORE_USER)
    public ResponseEntity<?> restoreUser(@PathVariable Long userId) {
        manageUserService.restoreUser(userId);
        return ResponseEntity.ok().body(Map.of(
            "message", UserMessages.USER_RESTORED_SUCCESS,
            "userId", userId,
            "timestamp", java.time.LocalDateTime.now()
        ));
    }
}
