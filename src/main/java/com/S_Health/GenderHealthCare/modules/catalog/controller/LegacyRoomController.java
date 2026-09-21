package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomConsultantDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.catalog.controller.RoomController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyRoomController {
    private final RoomService roomService;

    public LegacyRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_ROOM)
    public ResponseEntity<RoomDetailResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_ROOMS)
    public ResponseEntity<List<RoomDetailResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/specialization/{specializationId}")
    @Operation(summary = CatalogConstants.GET_ROOMS)
    public ResponseEntity<List<RoomDetailResponse>> getRoomsBySpecialization(@PathVariable Long specializationId) {
        return ResponseEntity.ok(roomService.getRoomsBySpecialization(specializationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_ROOM)
    public ResponseEntity<RoomDetailResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_ROOM)
    public ResponseEntity<RoomDetailResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_ROOM)
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roomId}/consultants")
    @Operation(summary = CatalogConstants.ASSIGN_CONSULTANT)
    public ResponseEntity<RoomConsultantDetailResponse> addConsultantToRoom(
            @PathVariable Long roomId,
            @Valid @RequestBody RoomConsultantRequest request) {
        return ResponseEntity.ok(roomService.addConsultantToRoom(roomId, request));
    }

    @DeleteMapping("/{roomId}/consultants/{assignmentId}")
    @Operation(summary = CatalogConstants.REMOVE_CONSULTANT)
    public ResponseEntity<Void> removeConsultantFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId) {
        roomService.removeConsultantFromRoom(roomId, assignmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roomId}/consultants")
    @Operation(summary = CatalogConstants.GET_ROOM_CONSULTANTS)
    public ResponseEntity<List<RoomConsultantDetailResponse>> getConsultantsInRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getConsultantsInRoom(roomId));
    }
}
