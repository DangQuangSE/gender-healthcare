package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.authentication.CreateUserRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.UpdateConsultantSpecializationRequest;
import com.S_Health.GenderHealthCare.modules.user.service.UserService;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserAccountResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserSpecializationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserManagementController {
    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create staff or consultant account")
    public ApiResponse<UserAccountResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.createUser(request), null);
    }

    @PostMapping("/{userId}/specializations")
    @Operation(summary = "Add consultant specializations")
    public ApiResponse<String> addSpecializations(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateConsultantSpecializationRequest request) {
        userService.addSpecializations(userId, request);
        return ApiResponse.success("Specializations added successfully", null);
    }

    @DeleteMapping("/{userId}/specializations/{specializationId}")
    @Operation(summary = "Remove consultant specialization")
    public ApiResponse<String> removeSpecialization(
            @PathVariable Long userId,
            @PathVariable Long specializationId) {
        userService.removeSpecialization(userId, specializationId);
        return ApiResponse.success("Specialization removed successfully", null);
    }

    @GetMapping("/{userId}/specializations")
    @Operation(summary = "Get consultant specializations")
    public ApiResponse<List<UserSpecializationResponse>> getSpecializations(@PathVariable Long userId) {
        return ApiResponse.success(userService.getConsultantSpecializations(userId), null);
    }

    @GetMapping
    @Operation(summary = "Get users by role")
    public ApiResponse<List<ConsultantResponse>> getUsersByRole(@RequestParam String role) {
        return ApiResponse.success(userService.getUsersByRole(role), null);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deactivate user")
    public ApiResponse<String> deactivateUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return ApiResponse.success("User deactivated successfully", null);
    }

    @PutMapping("/{userId}/restore")
    @Operation(summary = "Restore user")
    public ApiResponse<String> restoreUser(@PathVariable Long userId) {
        userService.restoreUser(userId);
        return ApiResponse.success("User restored successfully", null);
    }
}
