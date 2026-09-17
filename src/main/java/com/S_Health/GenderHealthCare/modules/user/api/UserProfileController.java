package com.S_Health.GenderHealthCare.modules.user.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.UserDTO;
import com.S_Health.GenderHealthCare.modules.user.application.UserFacade;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/me")
public class UserProfileController {
    private final UserFacade userFacade;

    public UserProfileController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping
    @Operation(summary = "Get current user profile")
    public ApiResponse<UserResponse> getProfile() {
        return ApiResponse.success(userFacade.getProfile(), null);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current user profile")
    public ApiResponse<UserResponse> updateProfile(@Valid @RequestBody UserDTO request) {
        return ApiResponse.success(userFacade.updateProfile(request), null);
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update current user avatar")
    public ApiResponse<UserResponse> updateAvatar(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(userFacade.updateAvatar(file), null);
    }
}
