package com.S_Health.GenderHealthCare.modules.scheduling.service;

import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.scheduling.enums.ScheduleStatus;
import com.S_Health.GenderHealthCare.modules.scheduling.domain.Schedule;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.scheduling.domain.ConsultantSlot;

import com.S_Health.GenderHealthCare.modules.scheduling.enums.SlotStatus;

import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.SlotResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleCancelRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleConsultantRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.ScheduleRegisterRequest;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.DoctorWorkingScheduleResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.ScheduleCancelResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.WorkDateSlotResponse;
import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.ScheduleRegisterResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.scheduling.SchedulingMessages;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence.AuthenticationRepository;
import com.S_Health.GenderHealthCare.modules.scheduling.infrastructure.persistence.ConsultantSlotRepository;
import com.S_Health.GenderHealthCare.modules.scheduling.infrastructure.persistence.ScheduleRepository;
import com.S_Health.GenderHealthCare.modules.catalog.service.ConfigValueService;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import com.S_Health.GenderHealthCare.utils.TimeSlotUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final ConsultantSlotRepository consultantSlotRepository;
    private final ConfigValueService configValueService;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            AuthenticationRepository authenticationRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            ConsultantSlotRepository consultantSlotRepository,
            ConfigValueService configValueService,
            ModelMapper modelMapper,
            AuthUtil authUtil) {
        this.scheduleRepository = scheduleRepository;
        this.authenticationRepository = authenticationRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.consultantSlotRepository = consultantSlotRepository;
        this.configValueService = configValueService;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }

    private Integer getMaxBooking() {
        return configValueService.getConfigValue(
                SchedulingMessages.MAX_BOOKING_CONFIG_KEY,
                SchedulingMessages.DEFAULT_MAX_BOOKING);
    }

    public List<WorkDateSlotResponse> getScheduleOfConsultant(ScheduleConsultantRequest request) {
        List<ConsultantSlot> slots = consultantSlotRepository.findByConsultantIdAndDateBetweenAndStatus(request.getConsultant_id(),
                request.getRangeDate().getFrom(),
                request.getRangeDate().getTo(), SlotStatus.ACTIVE);
        Map<LocalDate, List<SlotResponse>> slotMap = new HashMap<>();
        for (ConsultantSlot slot : slots) {
            SlotResponse slotDTO = new SlotResponse(
                    slot.getId(),
                    slot.getDate(),
                    slot.getStartTime(),
                    slot.getEndTime(),
                    slot.getMaxBooking(),
                    slot.getCurrentBooking(),
                    slot.getAvailableBooking()
            );
            slotMap.computeIfAbsent(slot.getDate(), day -> new ArrayList<>()).add(slotDTO);
        }
        return slotMap.entrySet().stream()
                .map(entry -> new WorkDateSlotResponse(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(WorkDateSlotResponse::getWorkDate))
                .toList();
    }

    public ScheduleRegisterResponse registerSchedule(ScheduleRegisterRequest request) {
        User consultant = authenticationRepository.findById(authUtil.getCurrentUserId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, SchedulingMessages.CONSULTANT_NOT_FOUND));
        List<ScheduleRegisterRequest.ScheduleItem> scheduleItems = request.getScheduleItems();
        for (ScheduleRegisterRequest.ScheduleItem item : scheduleItems) {
            if (!item.getWorkDate().isAfter(LocalDate.now())) {
                throw new DomainException(
                        ErrorCode.BAD_REQUEST,
                        SchedulingMessages.WORK_DATE_MUST_BE_FUTURE.formatted(item.getWorkDate()));
            }
        }
        Set<LocalDate> uniqueWorkDate = new HashSet<>();
        for (ScheduleRegisterRequest.ScheduleItem item : scheduleItems) {
            if (!uniqueWorkDate.add(item.getWorkDate())) {
                throw new DomainException(
                        ErrorCode.BAD_REQUEST,
                        SchedulingMessages.DUPLICATE_WORK_DATE.formatted(item.getWorkDate()));
            }
        }
        List<Schedule> schedules = new ArrayList<>();
        List<ConsultantSlot> consultantSlots = new ArrayList<>();
        List<ScheduleRegisterResponse.WorkDate> workDates = new ArrayList<>();
        for (ScheduleRegisterRequest.ScheduleItem item : scheduleItems) {
            Schedule schedule = new Schedule();
            schedule.setConsultant(consultant);
            schedule.setAvailable(true);
            schedule.setWorkDate(item.getWorkDate());
            schedule.setStartTime(item.getTimeSlot().getStartTime());
            schedule.setEndTime(item.getTimeSlot().getEndTime());
            schedule.setStatus(ScheduleStatus.ACTIVE);
            schedules.add(schedule);
            List<LocalTime> slots = TimeSlotUtils.generateSlots(
                    item.getTimeSlot().getStartTime(),
                    item.getTimeSlot().getEndTime(),
                    Duration.ofMinutes(SchedulingMessages.SLOT_DURATION_MINUTES));
            for (LocalTime start : slots) {
                LocalTime end = start.plusMinutes(SchedulingMessages.SLOT_DURATION_MINUTES);
                Integer maxBooking = getMaxBooking();
                ConsultantSlot consultantSlot = ConsultantSlot.builder()
                        .consultant(consultant)
                        .date(item.getWorkDate())
                        .startTime(start)
                        .endTime(end)
                        .availableBooking(maxBooking)
                        .maxBooking(maxBooking)
                        .currentBooking(0)
                        .status(SlotStatus.ACTIVE)
                        .isActive(true)
                        .build();
                consultantSlots.add(consultantSlot);
            }
            ScheduleRegisterResponse.WorkDate workDate = new ScheduleRegisterResponse.WorkDate();
            workDate.setDate(item.getWorkDate());
            workDate.setStart(item.getTimeSlot().getStartTime());
            workDate.setEnd(item.getTimeSlot().getEndTime());
            workDates.add(workDate);
        }
        scheduleRepository.saveAll(schedules);
        consultantSlotRepository.saveAll(consultantSlots);


        ScheduleRegisterResponse response = new ScheduleRegisterResponse();
        response.setConsultant_id(consultant.getId());
        response.setSchedules(workDates);
        return response;
    }

    //bác sĩ hủy lịch làm
    public ScheduleCancelResponse cancelSchedule(ScheduleCancelRequest request) {
        if (request.isCancelWholeDay() && request.getSlot() != null) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    SchedulingMessages.FULL_DAY_CANCEL_CANNOT_HAVE_SLOT);
        }
        if (!request.isCancelWholeDay() && request.getSlot() == null) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    SchedulingMessages.PARTIAL_DAY_CANCEL_REQUIRES_SLOT);
        }

        Long consultantId = authUtil.getCurrentUserId();
        LocalDate date = request.getDate();
        List<AppointmentDetail> affectedAppointments;

        if (request.isCancelWholeDay()) {
            affectedAppointments = appointmentDetailRepository
                    .findByConsultant_idAndSlotDate(consultantId, date);
            List<ConsultantSlot> slots = consultantSlotRepository.findByConsultantIdAndDate(consultantId, date);
            if (slots.isEmpty()) {
                throw new DomainException(ErrorCode.NOT_FOUND, SchedulingMessages.SLOTS_NOT_FOUND);
            }
            for (ConsultantSlot slot : slots) {
                slot.setIsActive(false);
                slot.setStatus(SlotStatus.DEACTIVE);
            }
            consultantSlotRepository.saveAll(slots);
        } else {
            LocalDateTime slotTime = LocalDateTime.of(date, request.getSlot());
            affectedAppointments = appointmentDetailRepository
                    .findByConsultant_idAndSlotTime(consultantId, slotTime);
            Optional<ConsultantSlot> slotOpt = consultantSlotRepository
                    .findByConsultantIdAndDateAndStartTime(consultantId, date, request.getSlot());
            if (slotOpt.isPresent()) {
                ConsultantSlot slot = slotOpt.get();
                slot.setIsActive(false);
                slot.setStatus(SlotStatus.DEACTIVE);
                consultantSlotRepository.save(slot);
            } else {
                throw new DomainException(ErrorCode.NOT_FOUND, SchedulingMessages.SLOT_NOT_FOUND);
            }
        }

        return new ScheduleCancelResponse(
                SchedulingMessages.SCHEDULE_CANCELLED,
                affectedAppointments.stream().map(a -> new ScheduleCancelResponse.AffectedAppointment(
                        a.getAppointment().getCustomer(),
                        a.getSlotTime().toLocalDate(),
                        SchedulingMessages.APPOINTMENT_CANCELLED
                )).toList()
        );
    }

    /**
     * Lấy danh sách bác sĩ làm việc theo ngày
     */
    public List<DoctorWorkingScheduleResponse> getDoctorsWorkingOnDate(LocalDate date) {
        // Lấy tất cả bác sĩ có role CONSULTANT và đang active
        List<User> doctors = authenticationRepository.findByRole(UserRole.CONSULTANT)
                .stream()
                .filter(User::isActive)
                .collect(Collectors.toList());

        List<DoctorWorkingScheduleResponse> result = new ArrayList<>();

        for (User doctor : doctors) {
            // Lấy các slot làm việc của bác sĩ trong ngày
            List<ConsultantSlot> slots = consultantSlotRepository.findByConsultantIdAndDate(doctor.getId(), date)
                    .stream()
                    .filter(slot -> slot.getStatus() == SlotStatus.ACTIVE && slot.getIsActive())
                    .collect(Collectors.toList());

            if (!slots.isEmpty()) {
                // Chuyển đổi slots thành SlotResponse
                List<SlotResponse> slotDTOs = slots.stream()
                        .map(slot -> new SlotResponse(
                                slot.getId(),
                                slot.getDate(),
                                slot.getStartTime(),
                                slot.getEndTime(),
                                slot.getMaxBooking(),
                                slot.getCurrentBooking(),
                                slot.getAvailableBooking()
                        ))
                        .sorted(Comparator.comparing(SlotResponse::getStartTime))
                        .collect(Collectors.toList());

                // Tạo DTO cho bác sĩ
                UserDetailResponse doctorDTO = modelMapper.map(doctor, UserDetailResponse.class);

                DoctorWorkingScheduleResponse doctorSchedule = new DoctorWorkingScheduleResponse();
                doctorSchedule.setDoctor(doctorDTO);
                doctorSchedule.setWorkDate(date);
                doctorSchedule.setSlots(slotDTOs);

                result.add(doctorSchedule);
            }
        }

        // Sắp xếp theo tên bác sĩ
        return result.stream()
                .sorted(Comparator.comparing(dto -> dto.getDoctor().getFullname()))
                .collect(Collectors.toList());
    }


}

