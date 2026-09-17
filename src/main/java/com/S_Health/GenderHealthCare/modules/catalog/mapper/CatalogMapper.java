package com.S_Health.GenderHealthCare.modules.catalog.mapper;

import com.S_Health.GenderHealthCare.dto.ServiceDTO;
import com.S_Health.GenderHealthCare.dto.SpecializationDTO;
import com.S_Health.GenderHealthCare.dto.TagDTO;
import com.S_Health.GenderHealthCare.dto.response.ComboResponse;
import com.S_Health.GenderHealthCare.dto.response.RoomConsultantDTO;
import com.S_Health.GenderHealthCare.dto.response.RoomDTO;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.SpecializationRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.TagRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceCreateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceUpdateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ComboServiceResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomConsultantResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.TagResponse;
import com.S_Health.GenderHealthCare.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Maps legacy DTOs into catalog module response DTOs.
 *
 * Keeping this conversion in one place makes the migration boundary explicit
 * and prevents controllers from depending on the old DTO package.
 */
@Component
public class CatalogMapper {
    private final UserMapper userMapper;

    public CatalogMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ServiceResponse toServiceResponse(ServiceDTO source) {
        if (source == null) {
            return null;
        }

        return ServiceResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .duration(source.getDuration())
                .type(source.getType())
                .price(source.getPrice())
                .discountPercent(source.getDiscountPercent())
                .isActive(source.getIsActive())
                .isCombo(source.getIsCombo())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .specializationIds(source.getSpecializationIds())
                .specializations(toSpecializationResponses(source.getSpecializations()))
                .subServiceIds(source.getSubServiceIds())
                .build();
    }

    public ServiceDTO toLegacyServiceDTO(ServiceCreateRequest source) {
        if (source == null) {
            return null;
        }

        ServiceDTO target = new ServiceDTO();
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setDuration(source.getDuration());
        target.setType(source.getType());
        target.setPrice(source.getPrice());
        target.setDiscountPercent(source.getDiscountPercent());
        target.setIsCombo(source.getIsCombo());
        target.setSpecializationIds(source.getSpecializationIds());
        target.setSubServiceIds(source.getSubServiceIds());
        return target;
    }

    public ServiceDTO toLegacyServiceDTO(ServiceUpdateRequest source) {
        if (source == null) {
            return null;
        }

        ServiceDTO target = new ServiceDTO();
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setDuration(source.getDuration());
        target.setType(source.getType());
        target.setPrice(source.getPrice());
        target.setDiscountPercent(source.getDiscountPercent());
        target.setSpecializationIds(source.getSpecializationIds());
        return target;
    }

    public com.S_Health.GenderHealthCare.dto.request.SpecializationRequest
    toLegacySpecializationRequest(SpecializationRequest source) {
        if (source == null) {
            return null;
        }

        com.S_Health.GenderHealthCare.dto.request.SpecializationRequest target =
                new com.S_Health.GenderHealthCare.dto.request.SpecializationRequest();
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        return target;
    }

    public com.S_Health.GenderHealthCare.dto.request.room.RoomRequest
    toLegacyRoomRequest(RoomRequest source) {
        if (source == null) {
            return null;
        }

        com.S_Health.GenderHealthCare.dto.request.room.RoomRequest target =
                new com.S_Health.GenderHealthCare.dto.request.room.RoomRequest();
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setSpecializationId(source.getSpecializationId());
        return target;
    }

    public com.S_Health.GenderHealthCare.dto.request.room.RoomConsultantRequest
    toLegacyRoomConsultantRequest(RoomConsultantRequest source) {
        if (source == null) {
            return null;
        }

        com.S_Health.GenderHealthCare.dto.request.room.RoomConsultantRequest target =
                new com.S_Health.GenderHealthCare.dto.request.room.RoomConsultantRequest();
        target.setConsultantId(source.getConsultantId());
        target.setWorkingDay(source.getWorkingDay());
        target.setStartTime(source.getStartTime());
        target.setEndTime(source.getEndTime());
        return target;
    }

    public com.S_Health.GenderHealthCare.dto.request.tag.TagRequest
    toLegacyTagRequest(TagRequest source) {
        if (source == null) {
            return null;
        }

        com.S_Health.GenderHealthCare.dto.request.tag.TagRequest target =
                new com.S_Health.GenderHealthCare.dto.request.tag.TagRequest();
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        return target;
    }

    public List<ServiceResponse> toServiceResponses(List<ServiceDTO> sources) {
        return sources.stream().map(this::toServiceResponse).toList();
    }

    public ComboServiceResponse toComboServiceResponse(ComboResponse source) {
        if (source == null) {
            return null;
        }

        return new ComboServiceResponse(
                toServiceResponse(source.getComboService()),
                toServiceResponses(source.getSubServices()));
    }

    public SpecializationResponse toSpecializationResponse(SpecializationDTO source) {
        if (source == null) {
            return null;
        }

        return SpecializationResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .isActive(source.getIsActive())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public List<SpecializationResponse> toSpecializationResponses(List<SpecializationDTO> sources) {
        if (sources == null) {
            return null;
        }
        return sources.stream().map(this::toSpecializationResponse).toList();
    }

    public RoomResponse toRoomResponse(RoomDTO source) {
        if (source == null) {
            return null;
        }

        return RoomResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .specialization(toSpecializationResponse(source.getSpecialization()))
                .consultants(toRoomConsultantResponses(source.getConsultants()))
                .isActive(source.isActive())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public List<RoomResponse> toRoomResponses(List<RoomDTO> sources) {
        return sources.stream().map(this::toRoomResponse).toList();
    }

    public RoomConsultantResponse toRoomConsultantResponse(RoomConsultantDTO source) {
        if (source == null) {
            return null;
        }

        return RoomConsultantResponse.builder()
                .id(source.getId())
                .consultant(userMapper.toResponse(source.getConsultant()))
                .startTime(source.getStartTime())
                .endTime(source.getEndTime())
                .isActive(source.isActive())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    public List<RoomConsultantResponse> toRoomConsultantResponses(List<RoomConsultantDTO> sources) {
        if (sources == null) {
            return null;
        }
        return sources.stream().map(this::toRoomConsultantResponse).toList();
    }

    public TagResponse toTagResponse(TagDTO source) {
        if (source == null) {
            return null;
        }

        return TagResponse.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }

    public List<TagResponse> toTagResponses(List<TagDTO> sources) {
        return sources.stream().map(this::toTagResponse).toList();
    }

}
