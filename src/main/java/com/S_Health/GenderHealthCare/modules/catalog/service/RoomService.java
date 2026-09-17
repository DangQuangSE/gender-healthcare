package com.S_Health.GenderHealthCare.modules.catalog.service;

import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Room;
import com.S_Health.GenderHealthCare.modules.catalog.domain.RoomConsultant;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;


import com.S_Health.GenderHealthCare.dto.UserDTO;
import com.S_Health.GenderHealthCare.dto.request.room.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.dto.request.room.RoomRequest;
import com.S_Health.GenderHealthCare.dto.response.RoomConsultantDTO;
import com.S_Health.GenderHealthCare.dto.response.RoomDTO;
import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.repository.RoomConsultantRepository;
import com.S_Health.GenderHealthCare.repository.RoomRepository;
import com.S_Health.GenderHealthCare.repository.SpecializationRepository;
import com.S_Health.GenderHealthCare.repository.AuthenticationRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public RoomDTO createRoom(RoomRequest request) {
        // Validate request
        if (roomRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw new AppException(CatalogConstants.ROOM_NAME_EXISTS);
        }

        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(() -> new AppException(CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(request.getSpecializationId())));
        Room room = new Room();
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setSpecialization(specialization);
        room.setActive(true);

        Room savedRoom = roomRepository.save(room);

        return convertToDTO(savedRoom);
    }

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getRoomsBySpecialization(Long specializationId) {
        Specialization specialization = specializationRepository.findById(specializationId)
                .orElseThrow(() -> new AppException(CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(specializationId)));

        return roomRepository.findBySpecializationAndIsActiveTrue(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RoomDTO getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new AppException(CatalogConstants.ROOM_INACTIVE);
        }

        return convertToDTO(room);
    }

    @Transactional
    public RoomDTO updateRoom(Long roomId, RoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new AppException(CatalogConstants.ROOM_INACTIVE);
        }

        if (!room.getName().equals(request.getName()) &&
                roomRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw new AppException(CatalogConstants.ROOM_NAME_EXISTS);
        }
        // Get specialization if changed
        if (!(room.getSpecialization().getId() == (request.getSpecializationId()))) {
            Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                    .orElseThrow(() -> new AppException(CatalogConstants.SPECIALIZATION_NOT_FOUND.formatted(request.getSpecializationId())));
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
                .orElseThrow(() -> new AppException(CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new AppException(CatalogConstants.ROOM_ALREADY_DELETED);
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
    public RoomConsultantDTO addConsultantToRoom(Long roomId, RoomConsultantRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(CatalogConstants.ROOM_NOT_FOUND));

        if (!room.isActive()) {
            throw new AppException(CatalogConstants.ROOM_INACTIVE);
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new AppException(CatalogConstants.WORKING_TIME_INVALID);
        }
        User consultant = authenticationRepository.findById(request.getConsultantId())
                .orElseThrow(() -> new AppException(CatalogConstants.CONSULTANT_NOT_FOUND));

        if (consultant.getRole() != UserRole.CONSULTANT) {
            throw new AppException(CatalogConstants.USER_NOT_CONSULTANT);
        }

        boolean hasSpecialization = consultant.getSpecializations().stream()
                .anyMatch(spec -> spec.getId() == (room.getSpecialization().getId()));

        if (!hasSpecialization) {
            throw new AppException(CatalogConstants.CONSULTANT_SPECIALIZATION_MISMATCH);
        }
        if (roomConsultantRepository.existsByRoomAndConsultantAndStartTimeAndEndTimeAndIsActiveTrue(
                room, consultant, request.getStartTime(), request.getEndTime())) {
            throw new AppException(CatalogConstants.WORKING_TIME_EXISTS);
        }
        RoomConsultant roomConsultant = new RoomConsultant();
        roomConsultant.setRoom(room);
        roomConsultant.setConsultant(consultant);
        roomConsultant.setStartTime(request.getStartTime());
        roomConsultant.setEndTime(request.getEndTime());
        roomConsultant.setActive(true);

        RoomConsultant savedAssignment = roomConsultantRepository.save(roomConsultant);

        return convertToConsultantDTO(savedAssignment);
    }

    @Transactional
    public void removeConsultantFromRoom(Long roomId, Long assignmentId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(CatalogConstants.ROOM_NOT_FOUND));
        RoomConsultant assignment = roomConsultantRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(CatalogConstants.WORKING_TIME_NOT_FOUND));
        if (!(assignment.getRoom().getId() == (room.getId()))) {
            throw new AppException(CatalogConstants.WORKING_TIME_WRONG_ROOM);
        }
        if (!assignment.isActive()) {
            throw new AppException(CatalogConstants.WORKING_TIME_ALREADY_DELETED);
        }
        // Deactivate assignment
        assignment.setActive(false);
        assignment.setUpdatedAt(LocalDateTime.now());
        roomConsultantRepository.save(assignment);
    }

    public List<RoomConsultantDTO> getConsultantsInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(CatalogConstants.ROOM_NOT_FOUND));

        return roomConsultantRepository.findByRoomAndIsActiveTrue(room).stream()
                .map(this::convertToConsultantDTO)
                .collect(Collectors.toList());
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = modelMapper.map(room, RoomDTO.class);

        // Get active consultants
        List<RoomConsultantDTO> consultants = roomConsultantRepository.findByRoomAndIsActiveTrue(room).stream()
                .map(this::convertToConsultantDTO)
                .collect(Collectors.toList());

        dto.setConsultants(consultants);
        return dto;
    }

    private RoomConsultantDTO convertToConsultantDTO(RoomConsultant roomConsultant) {
        RoomConsultantDTO dto = modelMapper.map(roomConsultant, RoomConsultantDTO.class);
        dto.setConsultant(modelMapper.map(roomConsultant.getConsultant(), UserDTO.class));
        return dto;
    }
}
