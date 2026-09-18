package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDTO;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.PatientHistoryDTO;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.AppointmentScheduleQuery;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.AppointmentStatusQuery;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.AppointmentDetailStatusRequest;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.UpdateAppointmentRequest;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.service.AppointmentService;
import com.S_Health.GenderHealthCare.modules.appointment.service.AppointmentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentQueryService appointmentQueryService;

    public AppointmentController(
            AppointmentService appointmentService,
            AppointmentQueryService appointmentQueryService) {
        this.appointmentService = appointmentService;
        this.appointmentQueryService = appointmentQueryService;
    }

    @GetMapping("/{id}")
    @Operation(summary = AppointmentMessages.GET_APPOINTMENT)
    public ApiResponse<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return ApiResponse.success(appointmentQueryService.getAppointmentById(id), null);
    }

    @GetMapping("/consultant-schedule")
    @Operation(summary = AppointmentMessages.GET_CONSULTANT_SCHEDULE)
    public ApiResponse<List<AppointmentDTO>> getMySchedule(
            @Valid @ModelAttribute AppointmentScheduleQuery request) {
        return ApiResponse.success(
                appointmentQueryService.getAppointmentsForConsultantOnDateByDetailStatus(request),
                null);
    }

    @GetMapping
    @Operation(summary = AppointmentMessages.GET_BY_STATUS)
    public ApiResponse<List<AppointmentDTO>> getAppointmentsByStatus(
            @Valid @ModelAttribute AppointmentStatusQuery request) {
        return ApiResponse.success(appointmentQueryService.getAppointmentsByStatus(request.getStatus()), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = AppointmentMessages.UPDATE_APPOINTMENT)
    public ApiResponse<AppointmentDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return ApiResponse.success(appointmentService.updateAppointment(id, request), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = AppointmentMessages.DELETE_APPOINTMENT)
    public ApiResponse<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ApiResponse.success(AppointmentMessages.APPOINTMENT_DELETED, null);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = AppointmentMessages.CANCEL_APPOINTMENT)
    public ApiResponse<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ApiResponse.success(AppointmentMessages.APPOINTMENT_CANCELED, null);
    }

    @PostMapping("/{id}/check-in")
    @Operation(summary = AppointmentMessages.CHECK_IN_APPOINTMENT)
    public ApiResponse<String> checkInAppointment(@PathVariable Long id) {
        appointmentService.checkInAppointment(id);
        return ApiResponse.success(AppointmentMessages.APPOINTMENT_CHECKED_IN, null);
    }

    @GetMapping("/{appointmentId}/history")
    @Operation(summary = AppointmentMessages.GET_PATIENT_HISTORY)
    public ApiResponse<PatientHistoryDTO> getPatientHistory(@PathVariable Long appointmentId) {
        return ApiResponse.success(
                appointmentQueryService.getPatientHistoryFromAppointment(appointmentId),
                null);
    }

    @PatchMapping("/details/{detailId}/status")
    @Operation(summary = AppointmentMessages.UPDATE_DETAIL_STATUS)
    public ApiResponse<String> updateAppointmentDetailStatus(
            @PathVariable Long detailId,
            @Valid @RequestBody AppointmentDetailStatusRequest request) {
        appointmentService.updateAppointmentDetailStatus(detailId, request.getStatus());
        return ApiResponse.success(AppointmentMessages.DETAIL_STATUS_UPDATED, null);
    }

    @PostMapping("/{id}/rating")
    @Operation(summary = AppointmentMessages.RATE_APPOINTMENT)
    public ApiResponse<String> rateAppointment(@PathVariable Long id) {
        appointmentService.updateIsRated(id);
        return ApiResponse.success(AppointmentMessages.APPOINTMENT_RATED, null);
    }
}
