package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.AppointmentDTO;
import com.S_Health.GenderHealthCare.dto.PatientHistoryDTO;
import com.S_Health.GenderHealthCare.dto.request.appointment.UpdateAppointmentRequest;
import com.S_Health.GenderHealthCare.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.service.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ApiResponse<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return ApiResponse.success(appointmentService.getAppointmentById(id), null);
    }

    @GetMapping("/consultant-schedule")
    @Operation(summary = "Get the current consultant schedule")
    public ApiResponse<List<AppointmentDTO>> getMySchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false)
            @Parameter(description = "Appointment detail status") AppointmentStatus status) {
        return ApiResponse.success(
                appointmentService.getAppointmentsForConsultantOnDateByDetailStatus(date, status),
                null);
    }

    @GetMapping
    @Operation(summary = "Get appointments by status")
    public ApiResponse<List<AppointmentDTO>> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {
        return ApiResponse.success(appointmentService.getAppointmentsByStatus(status), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an appointment")
    public ApiResponse<AppointmentDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return ApiResponse.success(appointmentService.updateAppointment(id, request), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment")
    public void cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
    }

    @PostMapping("/{id}/check-in")
    @Operation(summary = "Check in an appointment")
    public void checkInAppointment(@PathVariable Long id) {
        appointmentService.checkInAppointment(id);
    }

    @GetMapping("/{appointmentId}/history")
    @Operation(summary = "Get patient history from an appointment")
    public ApiResponse<PatientHistoryDTO> getPatientHistory(@PathVariable Long appointmentId) {
        return ApiResponse.success(
                appointmentService.getPatientHistoryFromAppointment(appointmentId),
                null);
    }

    @PatchMapping("/details/{detailId}/status")
    @Operation(summary = "Update an appointment detail status")
    public ApiResponse<String> updateAppointmentDetailStatus(
            @PathVariable Long detailId,
            @RequestParam AppointmentStatus status) {
        appointmentService.updateAppointmentDetailStatus(detailId, status);
        return ApiResponse.success("Appointment detail status updated", null);
    }

    @PostMapping("/{id}/rating")
    @Operation(summary = "Mark an appointment as rated")
    public void rateAppointment(@PathVariable Long id) {
        appointmentService.updateIsRated(id);
    }
}
