package com.S_Health.GenderHealthCare.modules.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Public user data returned by the user and catalog modules.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String fullname;
    private String phone;
    private String email;
    private String imageUrl;
    private String role;
    private LocalDate dateOfBirth;
    private String address;
    private List<Long> specializationIds;
    private String gender;
}
