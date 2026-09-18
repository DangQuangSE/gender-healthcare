package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomConsultantRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.RoomQuery;
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
    @Operation(summary = CatalogConstants.GET_ROOMS)
    public ApiResponse<List<RoomResponse>> getRooms(
            @Valid @ModelAttribute RoomQuery request) {
        return ApiResponse.success(catalogService.getRooms(request.getSpecializationId()), null);
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_ROOM)
    public ApiResponse<RoomResponse> getRoom(@PathVariable Long id) {
        return ApiResponse.success(catalogService.getRoom(id), null);
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_ROOM)
    public ApiResponse<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        return ApiResponse.success(catalogService.createRoom(request), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_ROOM)
    public ApiResponse<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequest request) {
        return ApiResponse.success(catalogService.updateRoom(id, request), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_ROOM)
    public ApiResponse<String> deleteRoom(@PathVariable Long id) {
        catalogService.deleteRoom(id);
        return ApiResponse.success(CatalogConstants.ROOM_DELETED, null);
    }

    @PostMapping("/{roomId}/consultants")
    @Operation(summary = CatalogConstants.ASSIGN_CONSULTANT)
    public ApiResponse<RoomConsultantResponse> addConsultantToRoom(
            @PathVariable Long roomId,
            @Valid @RequestBody RoomConsultantRequest request) {
        return ApiResponse.success(catalogService.addConsultantToRoom(roomId, request), null);
    }

    @DeleteMapping("/{roomId}/consultants/{assignmentId}")
    @Operation(summary = CatalogConstants.REMOVE_CONSULTANT)
    public ApiResponse<String> removeConsultantFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long assignmentId) {
        catalogService.removeConsultantFromRoom(roomId, assignmentId);
        return ApiResponse.success(CatalogConstants.CONSULTANT_REMOVED_FROM_ROOM, null);
    }

    @GetMapping("/{roomId}/consultants")
    @Operation(summary = CatalogConstants.GET_ROOM_CONSULTANTS)
    public ApiResponse<List<RoomConsultantResponse>> getRoomConsultants(@PathVariable Long roomId) {
        return ApiResponse.success(catalogService.getRoomConsultants(roomId), null);
    }
}
