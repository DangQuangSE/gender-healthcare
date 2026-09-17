package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.UserDTO;
import com.S_Health.GenderHealthCare.modules.user.service.UserService;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/me")
public class UserProfileController {
    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = UserMessages.GET_PROFILE)
    public ApiResponse<UserResponse> getProfile() {
        return ApiResponse.success(userService.getProfile(), null);
    }

    @PutMapping("/profile")
    @Operation(summary = UserMessages.UPDATE_PROFILE)
    public ApiResponse<UserResponse> updateProfile(@Valid @RequestBody UserDTO request) {
        return ApiResponse.success(userService.updateProfile(request), null);
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = UserMessages.UPDATE_AVATAR)
    public ApiResponse<UserResponse> updateAvatar(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(userService.updateAvatar(file), null);
    }
}
