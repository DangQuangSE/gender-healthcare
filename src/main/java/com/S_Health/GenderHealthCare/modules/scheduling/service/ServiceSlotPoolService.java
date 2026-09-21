package com.S_Health.GenderHealthCare.modules.scheduling.service;

import com.S_Health.GenderHealthCare.modules.scheduling.domain.ServiceSlotPool;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;
import com.S_Health.GenderHealthCare.modules.scheduling.domain.ConsultantSlot;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;
import com.S_Health.GenderHealthCare.modules.scheduling.enums.SlotStatus;

import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceDetailResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.SlotResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleServiceRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.WorkDateSlotResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.ScheduleServiceResponse;

import com.S_Health.GenderHealthCare.modules.scheduling.SchedulingMessages;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence.ServiceRepository;
import com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence.SpecializationRepository;
import com.S_Health.GenderHealthCare.modules.scheduling.infrastructure.persistence.ScheduleRepository;
import com.S_Health.GenderHealthCare.modules.scheduling.infrastructure.persistence.ServiceSlotPoolRepository;
import com.S_Health.GenderHealthCare.modules.scheduling.infrastructure.persistence.ConsultantSlotRepository;
import com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence.AuthenticationRepository;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceSlotPoolService {
    private final SpecializationRepository specializationRepository;
    private final AuthenticationRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final ServiceSlotPoolRepository serviceSlotPoolRepository;
    private final ConsultantSlotRepository consultantSlotRepository;

    public ServiceSlotPoolService(
            SpecializationRepository specializationRepository,
            AuthenticationRepository userRepository,
            ScheduleRepository scheduleRepository,
            ServiceRepository serviceRepository,
            ModelMapper modelMapper,
            ServiceSlotPoolRepository serviceSlotPoolRepository,
            ConsultantSlotRepository consultantSlotRepository) {
        this.specializationRepository = specializationRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
        this.serviceRepository = serviceRepository;
        this.modelMapper = modelMapper;
        this.serviceSlotPoolRepository = serviceSlotPoolRepository;
        this.consultantSlotRepository = consultantSlotRepository;
    }

    public ScheduleServiceResponse getSlotFreeService(ScheduleServiceRequest request) {
        Service service = serviceRepository.findById(request.getService_id())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, SchedulingMessages.SERVICE_NOT_FOUND));
        //lấy ra các consultant liên quan tới chuyên môm đó
        List<User> consultants = getConsultantInSpecialization(request.getService_id());
        //lấy ra ConsultantSlot của tất cả consultant
        List<ConsultantSlot> consultantSlots = consultantSlotRepository
                .findByConsultantInAndDateBetweenAndStatus(consultants, request.getRangeDate().getFrom(), request.getRangeDate().getTo(), SlotStatus.ACTIVE);
        //gom nhóm theo data và startTime
        Map<LocalDateTime, List<ConsultantSlot>> slotMap = consultantSlots.stream()
                .collect(Collectors.groupingBy(slot -> LocalDateTime.of(slot.getDate(), slot.getStartTime())));
        Map<LocalDate, List<SlotResponse>> dailySlotMap = new HashMap<>();
        for (Map.Entry<LocalDateTime, List<ConsultantSlot>> entry : slotMap.entrySet()) {
            LocalDateTime dt = entry.getKey();
            LocalDate date = dt.toLocalDate();
            LocalTime start = dt.toLocalTime();
            LocalTime end = start.plusMinutes(90);
            List<ConsultantSlot> slots = entry.getValue();
            // Tổng hợp thông tin booking
            int max = slots.stream().mapToInt(ConsultantSlot::getMaxBooking).sum();
            int current = slots.stream().mapToInt(ConsultantSlot::getCurrentBooking).sum();
            int available = Math.max(0, max - current);

            // 6. Tìm hoặc tạo ServiceSlotPool
            ServiceSlotPool serviceSlotPool = serviceSlotPoolRepository
                    .findByService_idAndDateAndStartTime(service.getId(), date, start)
                    .orElseGet(() -> ServiceSlotPool.builder()
                            .service(service)
                            .date(date)
                            .startTime(start)
                            .endTime(end)
                            .isActive(true)
                            .slotStatus(SlotStatus.ACTIVE)
                            .build());
            serviceSlotPool.setMaxBooking(max);
            serviceSlotPool.setCurrentBooking(current);
            serviceSlotPool.setAvailableBooking(available);
            serviceSlotPool.setIsActive(available > 0);
            serviceSlotPoolRepository.save(serviceSlotPool);
            // 7. Build DTO trả ra
            SlotResponse slotDTO = new SlotResponse(
                    serviceSlotPool.getId(),
                    date,
                    start,
                    end,
                    max,
                    current,
                    available
            );
            dailySlotMap.computeIfAbsent(date, d -> new ArrayList<>()).add(slotDTO);
        }
        // Chuyển về dạng ScheduleServiceResponse
        List<WorkDateSlotResponse> schedule = dailySlotMap.entrySet().stream()
                .map(e -> new WorkDateSlotResponse(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(WorkDateSlotResponse::getWorkDate))
                .toList();
        ServiceDetailResponse serviceDTO = modelMapper.map(service, ServiceDetailResponse.class);
        return new ScheduleServiceResponse(serviceDTO, schedule);
    }

    public List<User> getConsultantInSpecialization(long service_id) {
        List<Specialization> specializations = specializationRepository.findByServicesIdAndIsActiveTrue(service_id);
        List<Long> specializationIds = specializations.stream().map(Specialization::getId).toList();
        List<User> consultants = userRepository.findBySpecializations_IdInAndIsActive(specializationIds, true);
        return consultants;
    }
}
