package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDTO;
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
    public ResponseEntity<UserDTO> updateProfile(
            @Valid @RequestBody UserDTO request) {
        UserDTO updatedUser = userProfileService.updateUserProfile(request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    @Operation(summary = UserMessages.PROFILE_GET_LEGACY)
    public ResponseEntity<UserDTO> getProfile() {
        UserDTO user = userProfileService.getUserProfile();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/avatar")
    public ResponseEntity<UserDTO> updateAvatar(@RequestParam("file") MultipartFile file) {
        UserDTO updatedUser = userProfileService.updateAvatar(file);
        return ResponseEntity.ok(updatedUser);
    }
}
