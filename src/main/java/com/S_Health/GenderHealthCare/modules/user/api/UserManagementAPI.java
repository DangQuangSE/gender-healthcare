package com.S_Health.GenderHealthCare.modules.user.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.SpecializationDTO;
import com.S_Health.GenderHealthCare.dto.request.authentication.CreateUserRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.UpdateConsultantSpecializationRequest;
import com.S_Health.GenderHealthCare.dto.response.CreateUserResponse;
import com.S_Health.GenderHealthCare.dto.response.consultant.ConsultantDTO;
import com.S_Health.GenderHealthCare.modules.user.application.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserManagementAPI {
    private final UserFacade userFacade;

    public UserManagementAPI(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping
    @Operation(summary = "Create staff or consultant account")
    public ApiResponse<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userFacade.createUser(request), null);
    }

    @PostMapping("/{userId}/specializations")
    @Operation(summary = "Add consultant specializations")
    public ApiResponse<String> addSpecializations(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateConsultantSpecializationRequest request) {
        userFacade.addSpecializations(userId, request);
        return ApiResponse.success("Specializations added successfully", null);
    }

    @DeleteMapping("/{userId}/specializations/{specializationId}")
    @Operation(summary = "Remove consultant specialization")
    public ApiResponse<String> removeSpecialization(
            @PathVariable Long userId,
            @PathVariable Long specializationId) {
        userFacade.removeSpecialization(userId, specializationId);
        return ApiResponse.success("Specialization removed successfully", null);
    }

    @GetMapping("/{userId}/specializations")
    @Operation(summary = "Get consultant specializations")
    public ApiResponse<List<SpecializationDTO>> getSpecializations(@PathVariable Long userId) {
        return ApiResponse.success(userFacade.getConsultantSpecializations(userId), null);
    }

    @GetMapping
    @Operation(summary = "Get users by role")
    public ApiResponse<List<ConsultantDTO>> getUsersByRole(@RequestParam String role) {
        return ApiResponse.success(userFacade.getUsersByRole(role), null);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Deactivate user")
    public ApiResponse<String> deactivateUser(@PathVariable Long userId) {
        userFacade.softDeleteUser(userId);
        return ApiResponse.success("User deactivated successfully", null);
    }

    @PutMapping("/{userId}/restore")
    @Operation(summary = "Restore user")
    public ApiResponse<String> restoreUser(@PathVariable Long userId) {
        userFacade.restoreUser(userId);
        return ApiResponse.success("User restored successfully", null);
    }
}
