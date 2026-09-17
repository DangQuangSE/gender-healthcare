package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.request.CreateUserRequest;
import com.S_Health.GenderHealthCare.modules.user.dto.request.UpdateConsultantSpecializationRequest;
import com.S_Health.GenderHealthCare.modules.user.service.UserService;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
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
    @Operation(summary = UserMessages.CREATE_STAFF_ACCOUNT)
    public ApiResponse<UserAccountResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.createUser(request), null);
    }

    @PostMapping("/{userId}/specializations")
    @Operation(summary = UserMessages.ADD_SPECIALIZATIONS)
    public ApiResponse<String> addSpecializations(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateConsultantSpecializationRequest request) {
        userService.addSpecializations(userId, request);
        return ApiResponse.success(UserMessages.SPECIALIZATIONS_ADDED_SUCCESS, null);
    }

    @DeleteMapping("/{userId}/specializations/{specializationId}")
    @Operation(summary = UserMessages.REMOVE_SPECIALIZATION)
    public ApiResponse<String> removeSpecialization(
            @PathVariable Long userId,
            @PathVariable Long specializationId) {
        userService.removeSpecialization(userId, specializationId);
        return ApiResponse.success(UserMessages.SPECIALIZATION_REMOVED_SUCCESS, null);
    }

    @GetMapping("/{userId}/specializations")
    @Operation(summary = UserMessages.GET_SPECIALIZATIONS)
    public ApiResponse<List<UserSpecializationResponse>> getSpecializations(@PathVariable Long userId) {
        return ApiResponse.success(userService.getConsultantSpecializations(userId), null);
    }

    @GetMapping
    @Operation(summary = UserMessages.GET_USERS_BY_ROLE)
    public ApiResponse<List<ConsultantResponse>> getUsersByRole(@RequestParam String role) {
        return ApiResponse.success(userService.getUsersByRole(role), null);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = UserMessages.DEACTIVATE_USER)
    public ApiResponse<String> deactivateUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return ApiResponse.success(UserMessages.USER_DEACTIVATED_SUCCESS, null);
    }

    @PutMapping("/{userId}/restore")
    @Operation(summary = UserMessages.RESTORE_USER)
    public ApiResponse<String> restoreUser(@PathVariable Long userId) {
        userService.restoreUser(userId);
        return ApiResponse.success(UserMessages.USER_RESTORED_SUCCESS, null);
    }
}
