package com.S_Health.GenderHealthCare.modules.scheduling.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleCancelRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleRegisterRequest;
import com.S_Health.GenderHealthCare.dto.response.DoctorWorkingScheduleDTO;
import com.S_Health.GenderHealthCare.dto.response.ScheduleCancelResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleRegisterResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleServiceResponse;
import com.S_Health.GenderHealthCare.dto.response.WorkDateSlotResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.service.SchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class SchedulingController {
    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/consultants/{consultantId}")
    @Operation(summary = "Get a consultant schedule")
    public ApiResponse<List<WorkDateSlotResponse>> getConsultantSchedule(
            @PathVariable long consultantId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(
                schedulingService.getConsultantSchedule(consultantId, from, to),
                null);
    }

    @GetMapping("/services/{serviceId}/slots")
    @Operation(summary = "Get available slots for a service")
    public ApiResponse<ScheduleServiceResponse> getAvailableServiceSlots(
            @PathVariable long serviceId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(
                schedulingService.getAvailableServiceSlots(serviceId, from, to),
                null);
    }

    @PostMapping
    @Operation(summary = "Register a consultant schedule")
    public ApiResponse<ScheduleRegisterResponse> registerSchedule(
            @Valid @RequestBody ScheduleRegisterRequest request) {
        return ApiResponse.success(schedulingService.registerSchedule(request), null);
    }

    @PostMapping("/cancellations")
    @Operation(summary = "Cancel a consultant schedule")
    public ApiResponse<ScheduleCancelResponse> cancelSchedule(
            @Valid @RequestBody ScheduleCancelRequest request) {
        return ApiResponse.success(schedulingService.cancelSchedule(request), null);
    }

    @GetMapping("/doctors")
    @Operation(summary = "Get doctors working on a date")
    public ApiResponse<List<DoctorWorkingScheduleDTO>> getDoctorsWorkingOnDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(
                schedulingService.getDoctorsWorkingOnDate(date),
                null);
    }
}
