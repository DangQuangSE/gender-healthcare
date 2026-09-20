package com.S_Health.GenderHealthCare.modules.catalog.service;

import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Room;
import com.S_Health.GenderHealthCare.modules.catalog.domain.RoomConsultant;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;


import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomConsultantDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomDetailResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence.RoomConsultantRepository;
import com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence.RoomRepository;
import com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence.SpecializationRepository;
import com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence.AuthenticationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomConsultantRepository roomConsultantRepository;
    private final SpecializationRepository specializationRepository;
    private final AuthenticationRepository authenticationRepository;
    private final ModelMapper modelMapper;

    public RoomService(
            RoomRepository roomRepository,
            RoomConsultantRepository roomConsultantRepository,
            SpecializationRepository specializationRepository,
            AuthenticationRepository authenticationRepository,
            ModelMapper modelMapper) {
        this.roomRepository = roomRepository;
        this.roomConsultantRepository = roomConsultantRepository;
        this.specializationRepository = specializationRepository;
        this.authenticationRepository = authenticationRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public RoomDetailResponse createRoom(RoomRequest request) {
        // Validate request
        if (roomRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.ROOM_NAME_EXISTS);
        }

        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(request.getSpecializationId())));
        Room room = new Room();
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setSpecialization(specialization);
        room.setActive(true);

        Room savedRoom = roomRepository.save(room);

        return convertToDTO(savedRoom);
    }

    public List<RoomDetailResponse> getAllRooms() {
        return roomRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RoomDetailResponse> getRoomsBySpecialization(Long specializationId) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(specializationId)));

        return roomRepository.findBySpecializationAndIsActiveTrue(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoomDetailResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new DomainException(CatalogConstants.ROOM_INACTIVE);
        }

        return convertToDTO(room);
    }

    @Transactional
    public RoomDetailResponse updateRoom(Long roomId, RoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new DomainException(CatalogConstants.ROOM_INACTIVE);
        }

        if (!room.getName().equals(request.getName()) &&
                roomRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.ROOM_NAME_EXISTS);
        }
        // Get specialization if changed
        if (!(room.getSpecialization().getId() == (request.getSpecializationId()))) {
            Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                    .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(request.getSpecializationId())));
            room.setSpecialization(specialization);
        }

        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setUpdatedAt(LocalDateTime.now());
        Room updatedRoom = roomRepository.save(room);

        return convertToDTO(updatedRoom);
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.ROOM_ALREADY_DELETED);
        }

        // Deactivate all consultant assignments
        List<RoomConsultant> consultants = roomConsultantRepository.findByRoomAndIsActiveTrue(room);
        for (RoomConsultant consultant : consultants) {
            consultant.setActive(false);
            consultant.setUpdatedAt(LocalDateTime.now());
        }
        roomConsultantRepository.saveAll(consultants);

        // Deactivate room
        room.setActive(false);
        room.setUpdatedAt(LocalDateTime.now());
        roomRepository.save(room);
    }

    @Transactional
    public RoomConsultantDetailResponse addConsultantToRoom(Long roomId, RoomConsultantRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new DomainException(CatalogConstants.ROOM_INACTIVE);
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new DomainException(CatalogConstants.WORKING_TIME_INVALID);
        }
        User consultant = authenticationRepository.findById(request.getConsultantId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.CONSULTANT_NOT_FOUND));

        if (consultant.getRole() != UserRole.CONSULTANT) {
            throw new DomainException(CatalogConstants.USER_NOT_CONSULTANT);
        }

        boolean hasSpecialization = consultant.getSpecializations().stream()
                .anyMatch(spec -> spec.getId() == (room.getSpecialization().getId()));

        if (!hasSpecialization) {
            throw new DomainException(CatalogConstants.CONSULTANT_SPECIALIZATION_MISMATCH);
        }
        if (roomConsultantRepository.existsByRoomAndConsultantAndStartTimeAndEndTimeAndIsActiveTrue(
                room, consultant, request.getStartTime(), request.getEndTime())) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.WORKING_TIME_EXISTS);
        }
        RoomConsultant roomConsultant = new RoomConsultant();
        roomConsultant.setRoom(room);
        roomConsultant.setConsultant(consultant);
        roomConsultant.setStartTime(request.getStartTime());
        roomConsultant.setEndTime(request.getEndTime());
        roomConsultant.setActive(true);

        RoomConsultant savedAssignment = roomConsultantRepository.save(roomConsultant);

        return convertToConsultantDetailResponse(savedAssignment);
    }

    @Transactional
    public void removeConsultantFromRoom(Long roomId, Long assignmentId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.ROOM_NOT_FOUND));
        RoomConsultant assignment = roomConsultantRepository.findById(assignmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.WORKING_TIME_NOT_FOUND));
        if (!(assignment.getRoom().getId() == (room.getId()))) {
            throw new DomainException(CatalogConstants.WORKING_TIME_WRONG_ROOM);
        }
        if (!assignment.isActive()) {
            throw new DomainException(ErrorCode.CONFLICT, CatalogConstants.WORKING_TIME_ALREADY_DELETED);
        }
        // Deactivate assignment
        assignment.setActive(false);
        assignment.setUpdatedAt(LocalDateTime.now());
        roomConsultantRepository.save(assignment);
    }

    public List<RoomConsultantDetailResponse> getConsultantsInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, CatalogConstants.ROOM_NOT_FOUND));

        return roomConsultantRepository.findByRoomAndIsActiveTrue(room).stream()
                .map(this::convertToConsultantDetailResponse)
                .collect(Collectors.toList());
    }

    private RoomDetailResponse convertToDTO(Room room) {
        RoomDetailResponse dto = modelMapper.map(room, RoomDetailResponse.class);

        // Get active consultants
        List<RoomConsultantDetailResponse> consultants = roomConsultantRepository.findByRoomAndIsActiveTrue(room).stream()
                .map(this::convertToConsultantDetailResponse)
                .collect(Collectors.toList());

        dto.setConsultants(consultants);
        return dto;
    }

    private RoomConsultantDetailResponse convertToConsultantDetailResponse(RoomConsultant roomConsultant) {
        RoomConsultantDetailResponse dto = modelMapper.map(roomConsultant, RoomConsultantDetailResponse.class);
        dto.setConsultant(modelMapper.map(roomConsultant.getConsultant(), UserDetailResponse.class));
        return dto;
    }
}
