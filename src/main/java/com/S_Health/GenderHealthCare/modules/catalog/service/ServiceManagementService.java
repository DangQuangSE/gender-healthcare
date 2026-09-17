package com.S_Health.GenderHealthCare.modules.catalog.service;

import com.S_Health.GenderHealthCare.modules.catalog.domain.ComboItem;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;


import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceDTO;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationDTO;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ComboResponse;
import com.S_Health.GenderHealthCare.common.exception.ApiException;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.repository.ComboItemRepository;
import com.S_Health.GenderHealthCare.repository.ServiceRepository;
import com.S_Health.GenderHealthCare.repository.SpecializationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@org.springframework.stereotype.Service
public class ServiceManagementService {
    private final ServiceRepository serviceRepository;
    private final SpecializationRepository specializationRepository;
    private final ComboItemRepository comboItemRepository;
    private final ModelMapper modelMapper;

    public ServiceManagementService(
            ServiceRepository serviceRepository,
            SpecializationRepository specializationRepository,
            ComboItemRepository comboItemRepository,
            ModelMapper modelMapper) {
        this.serviceRepository = serviceRepository;
        this.specializationRepository = specializationRepository;
        this.comboItemRepository = comboItemRepository;
        this.modelMapper = modelMapper;
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ServiceDTO getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SERVICE_NOT_FOUND.formatted(id)));

        if (!service.getIsActive()) {
            throw new ApiException(CatalogConstants.SERVICE_INACTIVE);
        }

        return convertToDTO(service);
    }

    public List<ServiceDTO> searchServicesByName(String name) {
        return serviceRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesBySpecialization(Long specializationId) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(specializationId)));

        if (!specialization.getIsActive()) {
            throw new ApiException(CatalogConstants.SPECIALIZATION_INACTIVE);
        }

        return serviceRepository.findBySpecializationsContainingAndIsActiveTrue(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        // Kiểm tra tên dịch vụ có bị trùng không
        if (serviceRepository.existsByNameAndIsActiveTrue(serviceDTO.getName().trim())) {
            throw new ApiException(ErrorCode.CONFLICT, CatalogConstants.SERVICE_NAME_EXISTS);
        }

        // Tạo dịch vụ
        Service service = new Service();
        service.setName(serviceDTO.getName().trim());
        service.setDescription(serviceDTO.getDescription());
        service.setDuration(serviceDTO.getDuration());
        service.setType(serviceDTO.getType());
        service.setPrice(serviceDTO.getPrice());
        service.setDiscountPercent(serviceDTO.getDiscountPercent() != null ? serviceDTO.getDiscountPercent() : 0.0);
        service.setIsCombo(serviceDTO.getIsCombo() != null ? serviceDTO.getIsCombo() : false);
        service.setIsActive(true);

        // Thêm các chuyên môn vào dịch vụ
        if (serviceDTO.getSpecializationIds() != null && !serviceDTO.getSpecializationIds().isEmpty()) {
            List<Specialization> specializations = new ArrayList<>();

            for (Long specializationId : serviceDTO.getSpecializationIds()) {
                Specialization specialization = specializationRepository.findById(specializationId)
                        .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(specializationId)));

                if (!specialization.getIsActive()) {
                    throw new ApiException(CatalogConstants.SPECIALIZATION_INACTIVE_ID.formatted(specializationId));
                }

                specializations.add(specialization);
            }

            service.setSpecializations(specializations);
        }

        Service savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    @Transactional
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SERVICE_NOT_FOUND.formatted(id)));

        if (!service.getIsActive()) {
            throw new ApiException(CatalogConstants.SERVICE_INACTIVE_UPDATE);
        }

        // Kiểm tra tên dịch vụ có bị trùng không (nếu tên thay đổi)
        if (serviceDTO.getName() != null && !service.getName().equals(serviceDTO.getName().trim()) &&
                serviceRepository.existsByNameAndIsActiveTrue(serviceDTO.getName().trim())) {
            throw new ApiException(ErrorCode.CONFLICT, CatalogConstants.SERVICE_NAME_EXISTS);
        }

        // Cập nhật thông tin cơ bản
        if (serviceDTO.getName() != null) {
            service.setName(serviceDTO.getName().trim());
        }

        if (serviceDTO.getDescription() != null) {
            service.setDescription(serviceDTO.getDescription());
        }

        if (serviceDTO.getDuration() != null) {
            service.setDuration(serviceDTO.getDuration());
        }

        if (serviceDTO.getType() != null) {
            service.setType(serviceDTO.getType());
        }

        if (serviceDTO.getPrice() != null) {
            service.setPrice(serviceDTO.getPrice());
        }

        if (serviceDTO.getDiscountPercent() != null) {
            service.setDiscountPercent(serviceDTO.getDiscountPercent());
        }

        // Cập nhật danh sách chuyên môn
        if (serviceDTO.getSpecializationIds() != null && !serviceDTO.getSpecializationIds().isEmpty()) {
            List<Specialization> specializations = new ArrayList<>();

            for (Long specializationId : serviceDTO.getSpecializationIds()) {
                Specialization specialization = specializationRepository.findById(specializationId)
                        .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(specializationId)));

                if (!specialization.getIsActive()) {
                    throw new ApiException(CatalogConstants.SPECIALIZATION_INACTIVE_ID.formatted(specializationId));
                }

                specializations.add(specialization);
            }

            service.setSpecializations(specializations);
        }

        Service updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }

    @Transactional
    public ServiceDTO activateService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SERVICE_NOT_FOUND.formatted(id)));

        service.setIsActive(true);
        Service updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }

    @Transactional
    public ServiceDTO deactivateService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SERVICE_NOT_FOUND.formatted(id)));

        service.setIsActive(false);
        Service updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }

    @Transactional
    public ComboResponse createComboService(ServiceDTO serviceDTO) {
        // Kiểm tra tên dịch vụ combo có bị trùng không
        if (serviceRepository.existsByNameAndIsActiveTrue(serviceDTO.getName().trim())) {
            throw new ApiException(ErrorCode.CONFLICT, CatalogConstants.COMBO_NAME_EXISTS);
        }

        // Kiểm tra danh sách dịch vụ thành phần
        if (serviceDTO.getSubServiceIds() == null || serviceDTO.getSubServiceIds().isEmpty()) {
            throw new ApiException(CatalogConstants.COMBO_REQUIRES_ITEMS);
        }

        // Tạo dịch vụ combo
        Service comboService = new Service();
        comboService.setName(serviceDTO.getName().trim());
        comboService.setDescription(serviceDTO.getDescription());
        comboService.setDuration(serviceDTO.getDuration());
        comboService.setType(serviceDTO.getType());
        comboService.setDiscountPercent(serviceDTO.getDiscountPercent() != null ? serviceDTO.getDiscountPercent() : 0.0);
        comboService.setIsCombo(true);
        comboService.setIsActive(true);

        // Thêm các chuyên môn vào dịch vụ combo
        if (serviceDTO.getSpecializationIds() != null && !serviceDTO.getSpecializationIds().isEmpty()) {
            List<Specialization> specializations = new ArrayList<>();

            for (Long specializationId : serviceDTO.getSpecializationIds()) {
                Specialization specialization = specializationRepository.findById(specializationId)
                        .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(specializationId)));

                if (!specialization.getIsActive()) {
                    throw new ApiException(CatalogConstants.SPECIALIZATION_INACTIVE_ID.formatted(specializationId));
                }

                specializations.add(specialization);
            }

            comboService.setSpecializations(specializations);
        }

        // Lưu dịch vụ combo trước để có ID
        Service savedComboService = serviceRepository.save(comboService);

        // Tính tổng giá và tạo các ComboItem
        List<ComboItem> comboItems = new ArrayList<>();
        List<ServiceDTO> subServiceDTOs = new ArrayList<>();
        double totalPrice = 0.0;

        for (Long subServiceId : serviceDTO.getSubServiceIds()) {
            Service subService = serviceRepository.findById(subServiceId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, CatalogConstants.SUB_SERVICE_NOT_FOUND.formatted(subServiceId)));

            if (!subService.getIsActive()) {
                throw new ApiException(CatalogConstants.SUB_SERVICE_INACTIVE.formatted(subServiceId));
            }

            ComboItem comboItem = new ComboItem();
            comboItem.setComboService(savedComboService);
            comboItem.setSubService(subService);
            comboItem.setName(subService.getName());
            comboItems.add(comboItem);

            totalPrice += subService.getPrice();
            subServiceDTOs.add(convertToDTO(subService));
        }

        // Lưu các ComboItem
        comboItemRepository.saveAll(comboItems);

        // Tính giá cuối cùng sau khi áp dụng giảm giá
        double finalPrice = totalPrice * (1 - (savedComboService.getDiscountPercent() / 100));
        savedComboService.setPrice(finalPrice);
        savedComboService = serviceRepository.save(savedComboService);

        return new ComboResponse(convertToDTO(savedComboService), subServiceDTOs);
    }

    private ServiceDTO convertToDTO(Service service) {
        ServiceDTO dto = modelMapper.map(service, ServiceDTO.class);

        // Map danh sách chuyên môn
        if (service.getSpecializations() != null && !service.getSpecializations().isEmpty()) {
            List<SpecializationDTO> specializationDTOs = service.getSpecializations().stream()
                    .map(specialization -> modelMapper.map(specialization, SpecializationDTO.class))
                    .collect(Collectors.toList());

            dto.setSpecializations(specializationDTOs);

            List<Long> specializationIds = service.getSpecializations().stream()
                    .map(Specialization::getId)
                    .collect(Collectors.toList());

            dto.setSpecializationIds(specializationIds);
        }

        // Nếu là service combo, thêm danh sách subServiceIds
        if (Boolean.TRUE.equals(service.getIsCombo()) && service.getComboItems() != null && !service.getComboItems().isEmpty()) {
            List<Long> subServiceIds = service.getComboItems().stream()
                    .map(comboItem -> comboItem.getSubService().getId())
                    .collect(Collectors.toList());

            dto.setSubServiceIds(subServiceIds);
        }

        return dto;
    }
}
