package com.S_Health.GenderHealthCare.modules.catalog.service;

import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.SpecializationRequest;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.repository.SpecializationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@org.springframework.stereotype.Service
public class SpecializationService {
    private final SpecializationRepository specializationRepository;
    private final ModelMapper modelMapper;

    public SpecializationService(SpecializationRepository specializationRepository, ModelMapper modelMapper) {
        this.specializationRepository = specializationRepository;
        this.modelMapper = modelMapper;
    }

    public List<SpecializationDetailResponse> getAllSpecializations() {
        return specializationRepository.findByIsActiveTrue().stream()
                .map(specialization -> modelMapper.map(specialization, SpecializationDetailResponse.class))
                .collect(Collectors.toList());
    }

    public SpecializationDetailResponse getSpecializationById(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(id)));

        if (!specialization.getIsActive()) {
            throw new DomainException(CatalogConstants.SPECIALIZATION_INACTIVE);
        }

        return modelMapper.map(specialization, SpecializationDetailResponse.class);
    }

    public List<SpecializationDetailResponse> searchSpecializationsByName(String name) {
        return specializationRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream()
                .map(specialization -> modelMapper.map(specialization, SpecializationDetailResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public SpecializationDetailResponse createSpecialization(SpecializationRequest request) {
        // Kiểm tra tên chuyên môn có bị trùng không
        if (specializationRepository.existsByNameAndIsActiveTrue(request.getName().trim())) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.SPECIALIZATION_NAME_EXISTS);
        }

        Specialization specialization = new Specialization();
        specialization.setName(request.getName().trim());
        specialization.setDescription(request.getDescription());
        specialization.setIsActive(true);

        Specialization savedSpecialization = specializationRepository.save(specialization);
        return modelMapper.map(savedSpecialization, SpecializationDetailResponse.class);
    }

    @Transactional
    public SpecializationDetailResponse updateSpecialization(Long id, SpecializationRequest request) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(id)));

        if (!specialization.getIsActive()) {
            throw new DomainException(CatalogConstants.SPECIALIZATION_DELETED_UPDATE);
        }

        // Kiểm tra xem tên chuyên môn mới có trùng với chuyên môn khác không
        if (!specialization.getName().equalsIgnoreCase(request.getName().trim()) &&
                specializationRepository.existsByNameAndIsActiveTrue(request.getName().trim())) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.SPECIALIZATION_NAME_EXISTS);
        }

        specialization.setName(request.getName().trim());
        specialization.setDescription(request.getDescription());

        Specialization updatedSpecialization = specializationRepository.save(specialization);
        return modelMapper.map(updatedSpecialization, SpecializationDetailResponse.class);
    }

    @Transactional
    public void deleteSpecialization(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(id)));

        if (!specialization.getIsActive()) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.SPECIALIZATION_ALREADY_DELETED);
        }

        // Kiểm tra xem chuyên môn có được sử dụng không
        if (!specialization.getServices().isEmpty()) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.SPECIALIZATION_IN_USE_BY_SERVICES);
        }

        if (!specialization.getConsultants().isEmpty()) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.SPECIALIZATION_IN_USE_BY_CONSULTANTS);
        }
        specialization.setIsActive(false);
        specializationRepository.save(specialization);
    }
}


