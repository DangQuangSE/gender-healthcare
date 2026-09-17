package com.S_Health.GenderHealthCare.modules.user.service;

import com.S_Health.GenderHealthCare.common.validation.ImageUploadValidator;
import com.S_Health.GenderHealthCare.dto.UserDTO;
import com.S_Health.GenderHealthCare.dto.request.authentication.CreateUserRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.UpdateConsultantSpecializationRequest;
import com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserAccountResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserSpecializationResponse;
import com.S_Health.GenderHealthCare.modules.user.mapper.UserMapper;
import com.S_Health.GenderHealthCare.service.UserProfileService;
import com.S_Health.GenderHealthCare.service.authentication.ManageUserService;
import com.S_Health.GenderHealthCare.service.certification.CertificationService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Handles profile, certification, and user-management use cases.
 */
@Service
public class UserService {
    private final UserProfileService userProfileService;
    private final CertificationService certificationService;
    private final ManageUserService manageUserService;
    private final UserMapper userMapper;
    private final ImageUploadValidator imageUploadValidator;

    public UserService(
            UserProfileService userProfileService,
            CertificationService certificationService,
            ManageUserService manageUserService,
            UserMapper userMapper,
            ImageUploadValidator imageUploadValidator) {
        this.userProfileService = userProfileService;
        this.certificationService = certificationService;
        this.manageUserService = manageUserService;
        this.userMapper = userMapper;
        this.imageUploadValidator = imageUploadValidator;
    }

    public UserResponse getProfile() {
        return userMapper.toResponse(userProfileService.getUserProfile());
    }

    public UserResponse updateProfile(UserDTO request) {
        return userMapper.toResponse(userProfileService.updateUserProfile(request));
    }

    public UserResponse updateAvatar(MultipartFile file) {
        imageUploadValidator.validateRequired(file);
        return userMapper.toResponse(userProfileService.updateAvatar(file));
    }

    public CertificationResponse createCertification(String name, MultipartFile image) {
        imageUploadValidator.validateRequired(image);
        return userMapper.toCertificationResponse(certificationService.createCertification(name, image));
    }

    public CertificationResponse updateCertification(Long id, String name, MultipartFile image) {
        imageUploadValidator.validateOptional(image);
        return userMapper.toCertificationResponse(certificationService.updateCertification(id, name, image));
    }

    public List<CertificationResponse> getMyCertifications() {
        return userMapper.toCertificationResponses(certificationService.getMyCertifications());
    }

    public void deleteCertification(Long id) {
        certificationService.deleteCertification(id);
    }

    public UserAccountResponse createUser(CreateUserRequest request) {
        return userMapper.toAccountResponse(manageUserService.createStaffAccount(request));
    }

    public void addSpecializations(Long userId, UpdateConsultantSpecializationRequest request) {
        manageUserService.addSpecializationsToConsultant(userId, request.getSpecializationIds());
    }

    public void removeSpecialization(Long userId, Long specializationId) {
        manageUserService.removeSpecializationFromConsultant(userId, specializationId);
    }

    public List<UserSpecializationResponse> getConsultantSpecializations(Long userId) {
        return manageUserService.getConsultantSpecializations(userId).stream()
                .map(userMapper::toSpecializationResponse)
                .toList();
    }

    public List<ConsultantResponse> getUsersByRole(String role) {
        return userMapper.toConsultantResponses(manageUserService.getUsersByRole(role));
    }

    public List<ConsultantResponse> getConsultantsByService(Long serviceId) {
        return userMapper.toConsultantResponses(manageUserService.getConsultantsByService(serviceId));
    }

    public void softDeleteUser(Long userId) {
        manageUserService.softDeleteUser(userId);
    }

    public void restoreUser(Long userId) {
        manageUserService.restoreUser(userId);
    }
}
