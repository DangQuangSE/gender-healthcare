package com.S_Health.GenderHealthCare.modules.scheduling.service;



import com.S_Health.GenderHealthCare.dto.RangeDate;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleCancelRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleConsultantRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleRegisterRequest;
import com.S_Health.GenderHealthCare.dto.request.schedule.ScheduleServiceRequest;
import com.S_Health.GenderHealthCare.dto.response.DoctorWorkingScheduleDTO;
import com.S_Health.GenderHealthCare.dto.response.ScheduleCancelResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleRegisterResponse;
import com.S_Health.GenderHealthCare.dto.response.ScheduleServiceResponse;
import com.S_Health.GenderHealthCare.dto.response.WorkDateSlotResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Scheduling use cases exposed to the scheduling module controller.
 */
@Service
public class SchedulingService {
    private static final int DEFAULT_RANGE_WEEKS = 2;

    private final ScheduleService scheduleService;
    private final ServiceSlotPoolService serviceSlotPoolService;

    public SchedulingService(
            ScheduleService scheduleService,
            ServiceSlotPoolService serviceSlotPoolService) {
        this.scheduleService = scheduleService;
        this.serviceSlotPoolService = serviceSlotPoolService;
    }

    public List<WorkDateSlotResponse> getConsultantSchedule(
            long consultantId,
            LocalDate from,
            LocalDate to) {
        RangeDate rangeDate = buildRange(from, to);
        ScheduleConsultantRequest request = ScheduleConsultantRequest.builder()
                .consultant_id(consultantId)
                .rangeDate(rangeDate)
                .build();
        return scheduleService.getScheduleOfConsultant(request);
    }

    public ScheduleServiceResponse getAvailableServiceSlots(
            long serviceId,
            LocalDate from,
            LocalDate to) {
        ScheduleServiceRequest request = new ScheduleServiceRequest(
                serviceId,
                buildRange(from, to));
        return serviceSlotPoolService.getSlotFreeService(request);
    }

    public ScheduleRegisterResponse registerSchedule(ScheduleRegisterRequest request) {
        return scheduleService.registerSchedule(request);
    }

    public ScheduleCancelResponse cancelSchedule(ScheduleCancelRequest request) {
        return scheduleService.cancelSchedule(request);
    }

    public List<DoctorWorkingScheduleDTO> getDoctorsWorkingOnDate(LocalDate date) {
        return scheduleService.getDoctorsWorkingOnDate(date);
    }

    private RangeDate buildRange(LocalDate from, LocalDate to) {
        LocalDate start = from != null ? from : LocalDate.now();
        LocalDate end = to != null ? to : start.plusWeeks(DEFAULT_RANGE_WEEKS);
        return new RangeDate(start, end);
    }
}
