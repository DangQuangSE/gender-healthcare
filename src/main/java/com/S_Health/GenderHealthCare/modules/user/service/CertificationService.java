package com.S_Health.GenderHealthCare.modules.user.service;

import com.S_Health.GenderHealthCare.modules.user.domain.Certification;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.user.dto.request.CertificationRequest;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.common.message.CommonMessages;


import com.S_Health.GenderHealthCare.dto.response.certification.CertificationResponse;
import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.repository.CertificationRepository;
import com.S_Health.GenderHealthCare.integrations.storage.CloudinaryService;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificationService {
    private final CertificationRepository certificationRepository;
    private final CloudinaryService cloudinaryService;
    private final AuthUtil authUtil;

    public CertificationService(
            CertificationRepository certificationRepository,
            CloudinaryService cloudinaryService,
            AuthUtil authUtil) {
        this.certificationRepository = certificationRepository;
        this.cloudinaryService = cloudinaryService;
        this.authUtil = authUtil;
    }
    
    public CertificationResponse createCertification(CertificationRequest request) {
        User currentUser = authUtil.getCurrentUser();

        // Kiểm tra user có phải là consultant không
        if (!UserRole.CONSULTANT.equals(currentUser.getRole())) {
            throw new AppException(UserMessages.CERTIFICATION_ROLE_REQUIRED);
        }

        String imageUrl = null;
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                imageUrl = cloudinaryService.uploadCertificationImage(request.getImage());
            } catch (IOException e) {
                throw new AppException(CommonMessages.IMAGE_UPLOAD_FAILED.formatted(e.getMessage()));
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
                .orElseThrow(() -> new AppException(UserMessages.CERTIFICATION_NOT_FOUND_OR_FORBIDDEN));

        // Upload hình ảnh mới nếu có
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadCertificationImage(request.getImage());
                certification.setImage(imageUrl);
            } catch (IOException e) {
                throw new AppException(CommonMessages.IMAGE_UPLOAD_FAILED.formatted(e.getMessage()));
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
            throw new AppException(UserMessages.CERTIFICATION_ROLE_REQUIRED);
        }
        
        List<Certification> certifications = certificationRepository.findByConsultantAndIsActiveTrue(currentUser);
        
        return certifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    

    
    public void deleteCertification(Long id) {
        User currentUser = authUtil.getCurrentUser();
        
        Certification certification = certificationRepository.findByIdAndConsultantAndIsActiveTrue(id, currentUser)
                .orElseThrow(() -> new AppException(UserMessages.CERTIFICATION_NOT_FOUND_OR_FORBIDDEN));
        
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
