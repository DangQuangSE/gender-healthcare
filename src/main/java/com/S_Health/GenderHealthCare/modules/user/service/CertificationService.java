package com.S_Health.GenderHealthCare.modules.user.service;

import com.S_Health.GenderHealthCare.modules.user.domain.Certification;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.user.dto.request.CertificationRequest;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.common.message.CommonMessages;


import com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.repository.CertificationRepository;
import com.S_Health.GenderHealthCare.integrations.storage.ImageStorage;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final ImageStorage imageStorage;
    private final AuthUtil authUtil;

    public CertificationService(
            CertificationRepository certificationRepository,
            ImageStorage imageStorage,
            AuthUtil authUtil) {
        this.certificationRepository = certificationRepository;
        this.imageStorage = imageStorage;
        this.authUtil = authUtil;
    }
    
    public CertificationResponse createCertification(CertificationRequest request) {
        User currentUser = authUtil.getCurrentUser();

        // Kiểm tra user có phải là consultant không
        if (!UserRole.CONSULTANT.equals(currentUser.getRole())) {
            throw new DomainException(UserMessages.CERTIFICATION_ROLE_REQUIRED);
        }

        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                imageUrl = imageStorage.uploadCertificationImage(request.getImage());
            } catch (IOException e) {
                throw new DomainException(ErrorCode.INTERNAL_ERROR, CommonMessages.IMAGE_UPLOAD_FAILED, e);
            }
        }

        Certification certification = Certification.builder()
                .name(request.getName())
                .image(imageUrl)
                .consultant(currentUser)
                .isActive(true)
                .build();

        Certification saved = certificationRepository.save(certification);

        return mapToResponse(saved);
    }
    
    public CertificationResponse updateCertification(Long id, CertificationRequest request) {
        User currentUser = authUtil.getCurrentUser();

        Certification certification = certificationRepository.findByIdAndConsultantAndIsActiveTrue(id, currentUser)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.CERTIFICATION_NOT_FOUND_OR_FORBIDDEN));

        // Upload hình ảnh mới nếu có
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = imageStorage.uploadCertificationImage(request.getImage());
                certification.setImage(imageUrl);
            } catch (IOException e) {
                throw new DomainException(ErrorCode.INTERNAL_ERROR, CommonMessages.IMAGE_UPLOAD_FAILED, e);
            }
        }

        // Cập nhật thông tin
        certification.setName(request.getName());
        Certification updated = certificationRepository.save(certification);

        return mapToResponse(updated);
    }
    
    public List<CertificationResponse> getMyCertifications() {
        User currentUser = authUtil.getCurrentUser();
        
        if (!UserRole.CONSULTANT.equals(currentUser.getRole())) {
            throw new DomainException(UserMessages.CERTIFICATION_ROLE_REQUIRED);
        }
        
        List<Certification> certifications = certificationRepository.findByConsultantAndIsActiveTrue(currentUser);
        
        return certifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    

    
    public void deleteCertification(Long id) {
        User currentUser = authUtil.getCurrentUser();
        
        Certification certification = certificationRepository.findByIdAndConsultantAndIsActiveTrue(id, currentUser)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, UserMessages.CERTIFICATION_NOT_FOUND_OR_FORBIDDEN));
        
        certification.setActive(false);
        certificationRepository.save(certification);
    }
    
    private CertificationResponse mapToResponse(Certification certification) {
        return CertificationResponse.builder()
                .id(certification.getId())
                .name(certification.getName())
                .imageUrl(certification.getImage())
                .consultantId(certification.getConsultant().getId())
                .consultantName(certification.getConsultant().getFullname())
                .build();
    }
}
