package com.S_Health.GenderHealthCare.modules.scheduling.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleCancelRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleRegisterRequest;
import com.S_Health.GenderHealthCare.dto.response.DoctorWorkingScheduleDTO;
import com.S_Health.GenderHealthCare.dto.response.ScheduleCancelResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleRegisterResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleServiceResponse;
import com.S_Health.GenderHealthCare.dto.response.WorkDateSlotResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.SchedulingMessages;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleRangeRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.WorkingDoctorRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.service.SchedulingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
public class SchedulingController {
    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/consultants/{consultantId}")
    @Operation(summary = SchedulingMessages.GET_CONSULTANT_SCHEDULE)
    public ApiResponse<List<WorkDateSlotResponse>> getConsultantSchedule(
            @PathVariable long consultantId,
            @ModelAttribute ScheduleRangeRequest request) {
        return ApiResponse.success(
                schedulingService.getConsultantSchedule(consultantId, request),
                null);
    }

    @GetMapping("/services/{serviceId}/slots")
    @Operation(summary = SchedulingMessages.GET_AVAILABLE_SLOTS)
    public ApiResponse<ScheduleServiceResponse> getAvailableServiceSlots(
            @PathVariable long serviceId,
            @ModelAttribute ScheduleRangeRequest request) {
        return ApiResponse.success(
                schedulingService.getAvailableServiceSlots(serviceId, request),
                null);
    }

    @PostMapping
    @Operation(summary = SchedulingMessages.REGISTER_SCHEDULE)
    public ApiResponse<ScheduleRegisterResponse> registerSchedule(
            @Valid @RequestBody ScheduleRegisterRequest request) {
        return ApiResponse.success(schedulingService.registerSchedule(request), null);
    }

    @PostMapping("/cancellations")
    @Operation(summary = SchedulingMessages.CANCEL_SCHEDULE)
    public ApiResponse<ScheduleCancelResponse> cancelSchedule(
            @Valid @RequestBody ScheduleCancelRequest request) {
        return ApiResponse.success(schedulingService.cancelSchedule(request), null);
    }

    @GetMapping("/doctors")
    @Operation(summary = SchedulingMessages.GET_WORKING_DOCTORS)
    public ApiResponse<List<DoctorWorkingScheduleDTO>> getDoctorsWorkingOnDate(
            @Valid @ModelAttribute WorkingDoctorRequest request) {
        return ApiResponse.success(
                schedulingService.getDoctorsWorkingOnDate(request),
                null);
    }
}
