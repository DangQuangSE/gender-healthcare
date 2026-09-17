package com.S_Health.GenderHealthCare.modules.user.application;

import com.S_Health.GenderHealthCare.dto.SpecializationDTO;
import com.S_Health.GenderHealthCare.dto.UserDTO;
import com.S_Health.GenderHealthCare.dto.request.authentication.CreateUserRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.UpdateConsultantSpecializationRequest;
import com.S_Health.GenderHealthCare.dto.response.CreateUserResponse;
import com.S_Health.GenderHealthCare.dto.response.certification.CertificationResponse;
import com.S_Health.GenderHealthCare.dto.response.consultant.ConsultantDTO;
import com.S_Health.GenderHealthCare.service.UserService;
import com.S_Health.GenderHealthCare.service.authentication.ManageUserService;
import com.S_Health.GenderHealthCare.service.certification.CertificationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Application boundary for profile, certification, and user-management use cases.
 */
@Service
public class UserFacade {
    private final UserService userService;
    private final CertificationService certificationService;
    private final ManageUserService manageUserService;
    private final ModelMapper modelMapper;

    public UserFacade(
            UserService userService,
            CertificationService certificationService,
            ManageUserService manageUserService,
            ModelMapper modelMapper) {
        this.userService = userService;
        this.certificationService = certificationService;
        this.manageUserService = manageUserService;
        this.modelMapper = modelMapper;
    }

    public UserDTO getProfile() {
        return userService.getUserProfile();
    }

    public UserDTO updateProfile(UserDTO request) {
        return userService.updateUserProfile(request);
    }

    public UserDTO updateAvatar(MultipartFile file) {
        return userService.updateAvatar(file);
    }

    public CertificationResponse createCertification(String name, MultipartFile image) {
        return certificationService.createCertification(name, image);
    }

    public CertificationResponse updateCertification(Long id, String name, MultipartFile image) {
        return certificationService.updateCertification(id, name, image);
    }

    public List<CertificationResponse> getMyCertifications() {
        return certificationService.getMyCertifications();
    }

    public void deleteCertification(Long id) {
        certificationService.deleteCertification(id);
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        return manageUserService.createStaffAccount(request);
    }

    public void addSpecializations(Long userId, UpdateConsultantSpecializationRequest request) {
        manageUserService.addSpecializationsToConsultant(userId, request.getSpecializationIds());
    }

    public void removeSpecialization(Long userId, Long specializationId) {
        manageUserService.removeSpecializationFromConsultant(userId, specializationId);
    }

    public List<SpecializationDTO> getConsultantSpecializations(Long userId) {
        return manageUserService.getConsultantSpecializations(userId).stream()
                .map(specialization -> modelMapper.map(specialization, SpecializationDTO.class))
                .toList();
    }

    public List<ConsultantDTO> getUsersByRole(String role) {
        return manageUserService.getUsersByRole(role);
    }

    public void softDeleteUser(Long userId) {
        manageUserService.softDeleteUser(userId);
    }

    public void restoreUser(Long userId) {
        manageUserService.restoreUser(userId);
    }
}
