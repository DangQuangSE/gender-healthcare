package com.S_Health.GenderHealthCare.modules.user.dto.response;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    @NotBlank(message = UserMessages.FULLNAME_REQUIRED)
    @Size(max = 50, message = UserMessages.FULLNAME_TOO_LONG)
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = UserMessages.FULLNAME_INVALID)
    String fullname;
    long id;
    String phone;
    String email;
    MultipartFile img;
    String imageUrl;
    String role;
    LocalDate dateOfBirth;
    String address;
    List<Long> specializationIds;
    String gender;
}
