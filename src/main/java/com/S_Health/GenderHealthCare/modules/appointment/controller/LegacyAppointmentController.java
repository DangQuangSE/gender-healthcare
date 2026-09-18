package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.PatientHistoryResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.UpdateAppointmentRequest;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.AppointmentScheduleQuery;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.service.AppointmentService;
import com.S_Health.GenderHealthCare.modules.appointment.service.AppointmentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointment")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyAppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentQueryService appointmentQueryService;

    public LegacyAppointmentController(
            AppointmentService appointmentService,
            AppointmentQueryService appointmentQueryService) {
        this.appointmentService = appointmentService;
        this.appointmentQueryService = appointmentQueryService;
    }

    @GetMapping("{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentQueryService.getAppointmentById(id));
    }

    @GetMapping("/my-schedule")
    @Operation(summary = AppointmentMessages.GET_CONSULTANT_SCHEDULE)
    public ResponseEntity<List<AppointmentResponse>> getMySchedule(
            @Valid @ModelAttribute AppointmentScheduleQuery request) {
        List<AppointmentResponse> appointments = appointmentQueryService
                .getAppointmentsForConsultantOnDateByDetailStatus(request);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByStatus(
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentQueryService.getAppointmentsByStatus(status));
    }

    @PostMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointmentById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAppointmentById(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<Void> checkInAppointment(@PathVariable Long id) {
        appointmentService.checkInAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{appointmentId}/patient-history")
    public ResponseEntity<PatientHistoryResponse> getPatientHistory(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentQueryService.getPatientHistoryFromAppointment(appointmentId));
    }

    @PatchMapping("/detail/{detailId}/status")
    public ResponseEntity<String> updateAppointmentDetailStatus(
            @PathVariable Long detailId,
            @RequestParam AppointmentStatus status) {
        appointmentService.updateAppointmentDetailStatus(detailId, status);
        return ResponseEntity.ok(AppointmentMessages.DETAIL_STATUS_UPDATED);
    }

    @PutMapping("/{id}/rate")
    public ResponseEntity<Void> rateAppointment(@PathVariable Long id) {
        appointmentService.updateIsRated(id);
        return ResponseEntity.noContent().build();
    }
}
