package com.S_Health.GenderHealthCare.modules.user.mapper;

import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;

import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDTO;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationDTO;
import com.S_Health.GenderHealthCare.modules.user.dto.response.CreateUserResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.consultant.ConsultantCertification;
import com.S_Health.GenderHealthCare.modules.user.dto.response.consultant.ConsultantDTO;
import com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantCertificationResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserAccountResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserSpecializationResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts legacy user DTOs at the migration boundary.
 */
@Component
public class UserMapper {

    public UserResponse toResponse(UserDTO source) {
        if (source == null) {
            return null;
        }

        return UserResponse.builder()
                .id(source.getId())
                .fullname(source.getFullname())
                .phone(source.getPhone())
                .email(source.getEmail())
                .imageUrl(source.getImageUrl())
                .role(source.getRole())
                .dateOfBirth(source.getDateOfBirth())
                .address(source.getAddress())
                .specializationIds(source.getSpecializationIds())
                .gender(source.getGender())
                .build();
    }

    public UserSpecializationResponse toSpecializationResponse(SpecializationDTO source) {
        if (source == null) {
            return null;
        }

        return UserSpecializationResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .isActive(source.getIsActive())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public UserSpecializationResponse toSpecializationResponse(Specialization source) {
        if (source == null) {
            return null;
        }

        return UserSpecializationResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .isActive(source.getIsActive())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public UserAccountResponse toAccountResponse(CreateUserResponse source) {
        if (source == null) {
            return null;
        }

        return UserAccountResponse.builder()
                .id(source.getId())
                .fullname(source.getFullname())
                .email(source.getEmail())
                .phone(source.getPhone())
                .dateOfBirth(source.getDateOfBirth())
                .address(source.getAddress())
                .gender(source.getGender())
                .role(source.getRole())
                .imageUrl(source.getImageUrl())
                .isActive(source.isActive())
                .isVerified(source.isVerified())
                .createdAt(source.getCreatedAt())
                .specializations(source.getSpecializations() == null
                        ? null
                        : source.getSpecializations().stream()
                        .map(this::toSpecializationResponse)
                        .toList())
                .build();
    }

    public CertificationResponse toCertificationResponse(
            com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse source) {
        if (source == null) {
            return null;
        }

        return CertificationResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .imageUrl(source.getImageUrl())
                .createdAt(source.getCreatedAt())
                .consultantId(source.getConsultantId())
                .consultantName(source.getConsultantName())
                .build();
    }

    public List<CertificationResponse> toCertificationResponses(
            List<com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse> sources) {
        return sources.stream().map(this::toCertificationResponse).toList();
    }

    public ConsultantResponse toConsultantResponse(ConsultantDTO source) {
        if (source == null) {
            return null;
        }

        return ConsultantResponse.builder()
                .id(source.getId())
                .fullname(source.getFullname())
                .phone(source.getPhone())
                .email(source.getEmail())
                .imageUrl(source.getImageUrl())
                .dateOfBirth(source.getDateOfBirth())
                .address(source.getAddress())
                .specializationNames(source.getSpecializationNames())
                .certification(toConsultantCertificationResponses(source.getCertification()))
                .gender(source.getGender())
                .rating(source.getRating())
                .build();
    }

    public List<ConsultantResponse> toConsultantResponses(List<ConsultantDTO> sources) {
        return sources.stream().map(this::toConsultantResponse).toList();
    }

    private List<ConsultantCertificationResponse> toConsultantCertificationResponses(
            List<ConsultantCertification> sources) {
        if (sources == null) {
            return null;
        }

        return sources.stream()
                .map(source -> ConsultantCertificationResponse.builder()
                        .name(source.getName())
                        .imageUrl(source.getImageUrl())
                        .build())
                .toList();
    }
}
