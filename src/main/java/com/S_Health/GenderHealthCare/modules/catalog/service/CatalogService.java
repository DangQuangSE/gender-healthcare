package com.S_Health.GenderHealthCare.modules.catalog.service;

import com.S_Health.GenderHealthCare.entity.ConfigValue;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceCreateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceUpdateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.SpecializationRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.TagRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ComboServiceResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ConfigValueResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomConsultantResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.TagResponse;
import com.S_Health.GenderHealthCare.modules.catalog.mapper.CatalogMapper;
import com.S_Health.GenderHealthCare.modules.user.service.UserService;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application boundary for catalog reads and mutations.
 */
@Service
@Transactional(readOnly = true)
public class CatalogService {
    private final ServiceManagementService serviceManagementService;
    private final SpecializationService specializationService;
    private final RoomService roomService;
    private final TagService tagService;
    private final ConfigValueService configValueService;
    private final UserService userService;
    private final CatalogMapper catalogMapper;

    public CatalogService(
            ServiceManagementService serviceManagementService,
            SpecializationService specializationService,
            RoomService roomService,
            TagService tagService,
            ConfigValueService configValueService,
            UserService userService,
            CatalogMapper catalogMapper) {
        this.serviceManagementService = serviceManagementService;
        this.specializationService = specializationService;
        this.roomService = roomService;
        this.tagService = tagService;
        this.configValueService = configValueService;
        this.userService = userService;
        this.catalogMapper = catalogMapper;
    }

    public List<ServiceResponse> getServices() {
        return catalogMapper.toServiceResponses(serviceManagementService.getAllServices());
    }

    public List<ServiceResponse> getServices(String name, Long specializationId) {
        if (specializationId != null) {
            return getServicesBySpecialization(specializationId);
        }
        if (name != null && !name.isBlank()) {
            return searchServices(name);
        }
        return getServices();
    }

    public ServiceResponse getService(Long id) {
        return catalogMapper.toServiceResponse(serviceManagementService.getServiceById(id));
    }

    public List<ServiceResponse> searchServices(String name) {
        return catalogMapper.toServiceResponses(serviceManagementService.searchServicesByName(name));
    }

    public List<ServiceResponse> getServicesBySpecialization(Long specializationId) {
        return catalogMapper.toServiceResponses(
                serviceManagementService.getServicesBySpecialization(specializationId));
    }

    @Transactional
    public ServiceResponse createService(ServiceCreateRequest request) {
        return catalogMapper.toServiceResponse(
                serviceManagementService.createService(catalogMapper.toLegacyServiceDTO(request)));
    }

    @Transactional
    public ServiceResponse updateService(Long id, ServiceUpdateRequest request) {
        return catalogMapper.toServiceResponse(
                serviceManagementService.updateService(id, catalogMapper.toLegacyServiceDTO(request)));
    }

    @Transactional
    public ServiceResponse activateService(Long id) {
        return catalogMapper.toServiceResponse(serviceManagementService.activateService(id));
    }

    @Transactional
    public ServiceResponse deactivateService(Long id) {
        return catalogMapper.toServiceResponse(serviceManagementService.deactivateService(id));
    }

    @Transactional
    public ComboServiceResponse createComboService(ServiceCreateRequest request) {
        return catalogMapper.toComboServiceResponse(
                serviceManagementService.createComboService(catalogMapper.toLegacyServiceDTO(request)));
    }

    public List<SpecializationResponse> getSpecializations() {
        return catalogMapper.toSpecializationResponses(specializationService.getAllSpecializations());
    }

    public List<SpecializationResponse> getSpecializations(String name) {
        if (name != null && !name.isBlank()) {
            return searchSpecializations(name);
        }
        return getSpecializations();
    }

    public SpecializationResponse getSpecialization(Long id) {
        return catalogMapper.toSpecializationResponse(specializationService.getSpecializationById(id));
    }

    public List<SpecializationResponse> searchSpecializations(String name) {
        return catalogMapper.toSpecializationResponses(
                specializationService.searchSpecializationsByName(name));
    }

    @Transactional
    public SpecializationResponse createSpecialization(SpecializationRequest request) {
        return catalogMapper.toSpecializationResponse(
                specializationService.createSpecialization(
                        catalogMapper.toLegacySpecializationRequest(request)));
    }

    @Transactional
    public SpecializationResponse updateSpecialization(Long id, SpecializationRequest request) {
        return catalogMapper.toSpecializationResponse(
                specializationService.updateSpecialization(
                        id,
                        catalogMapper.toLegacySpecializationRequest(request)));
    }

    @Transactional
    public void deleteSpecialization(Long id) {
        specializationService.deleteSpecialization(id);
    }

    public List<RoomResponse> getRooms() {
        return catalogMapper.toRoomResponses(roomService.getAllRooms());
    }

    public List<RoomResponse> getRooms(Long specializationId) {
        if (specializationId != null) {
            return getRoomsBySpecialization(specializationId);
        }
        return getRooms();
    }

    public List<RoomResponse> getRoomsBySpecialization(Long specializationId) {
        return catalogMapper.toRoomResponses(roomService.getRoomsBySpecialization(specializationId));
    }

    public RoomResponse getRoom(Long id) {
        return catalogMapper.toRoomResponse(roomService.getRoomById(id));
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        return catalogMapper.toRoomResponse(
                roomService.createRoom(catalogMapper.toLegacyRoomRequest(request)));
    }

    @Transactional
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        return catalogMapper.toRoomResponse(
                roomService.updateRoom(id, catalogMapper.toLegacyRoomRequest(request)));
    }

    @Transactional
    public void deleteRoom(Long id) {
        roomService.deleteRoom(id);
    }

    @Transactional
    public RoomConsultantResponse addConsultantToRoom(Long roomId, RoomConsultantRequest request) {
        return catalogMapper.toRoomConsultantResponse(
                roomService.addConsultantToRoom(
                        roomId,
                        catalogMapper.toLegacyRoomConsultantRequest(request)));
    }

    @Transactional
    public void removeConsultantFromRoom(Long roomId, Long assignmentId) {
        roomService.removeConsultantFromRoom(roomId, assignmentId);
    }

    public List<RoomConsultantResponse> getRoomConsultants(Long roomId) {
        return catalogMapper.toRoomConsultantResponses(roomService.getConsultantsInRoom(roomId));
    }

    public List<TagResponse> getTags() {
        return catalogMapper.toTagResponses(tagService.getAllTags());
    }

    public TagResponse getTag(Long id) {
        return catalogMapper.toTagResponse(tagService.getTagById(id));
    }

    @Transactional
    public TagResponse createTag(TagRequest request) {
        return catalogMapper.toTagResponse(tagService.createTag(catalogMapper.toLegacyTagRequest(request)));
    }

    @Transactional
    public TagResponse updateTag(Long id, TagRequest request) {
        return catalogMapper.toTagResponse(
                tagService.updateTag(id, catalogMapper.toLegacyTagRequest(request)));
    }

    @Transactional
    public void deleteTag(Long id) {
        tagService.deleteTag(id);
    }

    public List<ConfigValueResponse> getConfigs() {
        return configValueService.getAllConfigs().stream()
                .map(this::toConfigValueResponse)
                .toList();
    }

    @Transactional
    public ConfigValueResponse createConfig(String name, Integer value) {
        return toConfigValueResponse(configValueService.createConfig(name, value));
    }

    @Transactional
    public ConfigValueResponse updateConfig(Long id, Integer value) {
        return toConfigValueResponse(configValueService.updateConfig(id, value));
    }

    @Transactional
    public void deleteConfig(Long id) {
        configValueService.deleteConfig(id);
    }

    public List<ConsultantResponse> getConsultants() {
        return userService.getUsersByRole(CatalogConstants.CONSULTANT_ROLE);
    }

    public List<ConsultantResponse> getConsultants(Long serviceId) {
        if (serviceId != null) {
            return getConsultantsByService(serviceId);
        }
        return getConsultants();
    }

    public List<ConsultantResponse> getConsultantsByService(Long serviceId) {
        return userService.getConsultantsByService(serviceId);
    }

    private ConfigValueResponse toConfigValueResponse(ConfigValue configValue) {
        return new ConfigValueResponse(configValue.getId(), configValue.getName(), configValue.getValue());
    }
}
