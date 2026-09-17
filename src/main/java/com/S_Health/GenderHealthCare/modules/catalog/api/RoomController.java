package com.S_Health.GenderHealthCare.modules.catalog.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomConsultantResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.RoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final CatalogService catalogService;

    public RoomController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = "Get active rooms with optional specialization filter")
    public ApiResponse<List<RoomResponse>> getRooms(
            @RequestParam(required = false) Long specializationId) {
        return ApiResponse.success(catalogService.getRooms(specializationId), null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ApiResponse<RoomResponse> getRoom(@PathVariable Long id) {
        return ApiResponse.success(catalogService.getRoom(id), null);
    }

    @PostMapping
    @Operation(summary = "Create room")
    public ApiResponse<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        return ApiResponse.success(catalogService.createRoom(request), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room")
    public ApiResponse<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request) {
        return ApiResponse.success(catalogService.updateRoom(id, request), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room")
    public ApiResponse<String> deleteRoom(@PathVariable Long id) {
        catalogService.deleteRoom(id);
        return ApiResponse.success(CatalogConstants.ROOM_DELETED, null);
    }

    @PostMapping("/{roomId}/consultants")
    @Operation(summary = "Assign consultant to room")
    public ApiResponse<RoomConsultantResponse> addConsultantToRoom(
            @PathVariable Long roomId,
            @Valid @RequestBody RoomConsultantRequest request) {
        return ApiResponse.success(catalogService.addConsultantToRoom(roomId, request), null);
    }

    @DeleteMapping("/{roomId}/consultants/{assignmentId}")
    @Operation(summary = "Remove consultant from room")
    public ApiResponse<String> removeConsultantFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId) {
        catalogService.removeConsultantFromRoom(roomId, assignmentId);
        return ApiResponse.success(CatalogConstants.CONSULTANT_REMOVED_FROM_ROOM, null);
    }

    @GetMapping("/{roomId}/consultants")
    @Operation(summary = "Get room consultants")
    public ApiResponse<List<RoomConsultantResponse>> getRoomConsultants(@PathVariable Long roomId) {
        return ApiResponse.success(catalogService.getRoomConsultants(roomId), null);
    }
}
