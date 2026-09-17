package com.S_Health.GenderHealthCare.modules.scheduling.service;



import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.RangeDate;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleCancelRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleConsultantRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleRegisterRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleServiceRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.DoctorWorkingScheduleDTO;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.ScheduleCancelResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.ScheduleRegisterResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.ScheduleServiceResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.WorkDateSlotResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleRangeRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.WorkingDoctorRequest;
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
            ScheduleRangeRequest rangeRequest) {
        RangeDate rangeDate = buildRange(rangeRequest.getFrom(), rangeRequest.getTo());
        ScheduleConsultantRequest scheduleRequest = ScheduleConsultantRequest.builder()
                .consultant_id(consultantId)
                .rangeDate(rangeDate)
                .build();
        return scheduleService.getScheduleOfConsultant(scheduleRequest);
    }

    public ScheduleServiceResponse getAvailableServiceSlots(
            long serviceId,
            ScheduleRangeRequest rangeRequest) {
        ScheduleServiceRequest serviceRequest = new ScheduleServiceRequest(
                serviceId,
                buildRange(rangeRequest.getFrom(), rangeRequest.getTo()));
        return serviceSlotPoolService.getSlotFreeService(serviceRequest);
    }

    public ScheduleRegisterResponse registerSchedule(ScheduleRegisterRequest request) {
        return scheduleService.registerSchedule(request);
    }

    public ScheduleCancelResponse cancelSchedule(ScheduleCancelRequest request) {
        return scheduleService.cancelSchedule(request);
    }

    public List<DoctorWorkingScheduleDTO> getDoctorsWorkingOnDate(WorkingDoctorRequest request) {
        return scheduleService.getDoctorsWorkingOnDate(request.getDate());
    }

    private RangeDate buildRange(LocalDate from, LocalDate to) {
        LocalDate start = from != null ? from : LocalDate.now();
        LocalDate end = to != null ? to : start.plusWeeks(DEFAULT_RANGE_WEEKS);
        return new RangeDate(start, end);
    }
}
