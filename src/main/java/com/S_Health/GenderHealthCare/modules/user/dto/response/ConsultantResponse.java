package com.S_Health.GenderHealthCare.modules.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantResponse {
    private long id;
    private String fullname;
    private String phone;
    private String email;
    private String imageUrl;
    private LocalDate dateOfBirth;
    private String address;
    private List<String> specializationNames;
    private List<ConsultantCertificationResponse> certification;
    private String gender;
    private double rating;
}
