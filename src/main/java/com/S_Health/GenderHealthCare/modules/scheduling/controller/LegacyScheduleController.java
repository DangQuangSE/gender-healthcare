package com.S_Health.GenderHealthCare.modules.scheduling.controller;

import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleCancelRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleRegisterRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleConsultantRequest;
import com.S_Health.GenderHealthCare.dto.response.DoctorWorkingScheduleDTO;
import com.S_Health.GenderHealthCare.dto.response.WorkDateSlotResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleServiceResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.service.ServiceSlotPoolService;
import com.S_Health.GenderHealthCare.modules.scheduling.service.ScheduleService;
import com.S_Health.GenderHealthCare.modules.scheduling.service.SchedulingService;
import com.S_Health.GenderHealthCare.modules.scheduling.SchedulingMessages;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleRangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyScheduleController {
    private final ScheduleService scheduleService;
    private final SchedulingService schedulingService;

    public LegacyScheduleController(
            ScheduleService scheduleService,
            SchedulingService schedulingService) {
        this.scheduleService = scheduleService;
        this.schedulingService = schedulingService;
    }

    @GetMapping("/view")
    public ResponseEntity getScheduleOfConsultant(
            @RequestParam(value = "consultant_id") long id,
            @ModelAttribute ScheduleRangeRequest request) {
        List<WorkDateSlotResponse> result = schedulingService.getConsultantSchedule(id, request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/slot-free-service")
    public ResponseEntity getAvailableSlots(
            @RequestParam Long service_id,
            @ModelAttribute ScheduleRangeRequest request
    ) {
        ScheduleServiceResponse response = schedulingService.getAvailableServiceSlots(service_id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity registerSchedule(@RequestBody ScheduleRegisterRequest request) {
        return ResponseEntity.ok(scheduleService.registerSchedule(request));
    }
    @PostMapping("/cancel")
    public ResponseEntity cancelSchedule(@RequestBody ScheduleCancelRequest request) {
        return ResponseEntity.ok(scheduleService.cancelSchedule(request));
    }

    @GetMapping("/doctors-working")
    @Operation(summary = SchedulingMessages.GET_WORKING_DOCTORS)
    public ResponseEntity<List<DoctorWorkingScheduleDTO>> getDoctorsWorkingOnDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DoctorWorkingScheduleDTO> result = scheduleService.getDoctorsWorkingOnDate(date);
        return ResponseEntity.ok(result);
    }
}
