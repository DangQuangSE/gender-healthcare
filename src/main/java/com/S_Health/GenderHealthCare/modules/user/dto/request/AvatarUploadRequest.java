package com.S_Health.GenderHealthCare.modules.user.dto.request;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Multipart request used to update the current user's avatar.
 */
@Getter
@Setter
public class AvatarUploadRequest {
    @NotNull(message = UserMessages.AVATAR_REQUIRED)
    private MultipartFile file;
}
