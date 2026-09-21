package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.request.UserProfileUpdateRequest;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import com.S_Health.GenderHealthCare.modules.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/me")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.user.controller.UserProfileController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyProfileController {
    private final UserProfileService userProfileService;

    public LegacyProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PutMapping("/profile")
    @Operation(summary = UserMessages.PROFILE_UPDATE_LEGACY)
    public ResponseEntity<UserDetailResponse> updateProfile(
            @Valid @RequestBody UserProfileUpdateRequest request) {
        UserDetailResponse updatedUser = userProfileService.updateUserProfile(request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    @Operation(summary = UserMessages.PROFILE_GET_LEGACY)
    public ResponseEntity<UserDetailResponse> getProfile() {
        UserDetailResponse user = userProfileService.getUserProfile();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/avatar")
    public ResponseEntity<UserDetailResponse> updateAvatar(@RequestParam("file") MultipartFile file) {
        UserDetailResponse updatedUser = userProfileService.updateAvatar(file);
        return ResponseEntity.ok(updatedUser);
    }
}
